package com.volcano.sync.service;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SyncService implements ISyncService {
    private static final ThreadLocal<TransactionStatus> TRANSACTION_STATUS = new ThreadLocal<>();

    private static final ConcurrentHashMap<TransactionStatus, List<String>> CONN_SQLS = new ConcurrentHashMap();

    private static final Set<TransactionStatus> ACTIVE_TRANSACTIONS = new LinkedHashSet<>();

    @Resource
    DataSource dataSource;

    @PostConstruct
    public void init() {
        checkActiveConnections();
    }

    @Override
    public TransactionStatus getTransactionStatus() {
        TransactionStatus status = TRANSACTION_STATUS.get();
        if (status == null) {
            status = TransactionAspectSupport.currentTransactionStatus();
            TRANSACTION_STATUS.set(status);
        }

        return status;
    }

    @Override
    public List<String> buildSqls(TransactionStatus status, Connection connection, String sql) {
        List<String> sqls = CONN_SQLS.get(status);
        if (sql == null) {
            return sqls;
        }

        try {
            if (connection == null || connection.getAutoCommit()) {
                commit(sql);
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (CollectionUtils.isEmpty(sqls)) {
            sqls = new ArrayList<>();
            CONN_SQLS.put(status, sqls);
        }

        if (sql != null) {
            sqls.add(sql);
        }

        ACTIVE_TRANSACTIONS.add(status);
        return sqls;
    }

    private void checkActiveConnections() {
        new Thread("Check Transaction Status Thread") {
            @SneakyThrows
            @Override
            public void run() {
                for (; ; ) {
                    if (!ACTIVE_TRANSACTIONS.isEmpty()) {
                        try {
                            Iterator<TransactionStatus> iterator = ACTIVE_TRANSACTIONS.iterator();
                            while (iterator != null && iterator.hasNext()) {
                                TransactionStatus status = iterator.next();
                                if (status.isCompleted()) {
                                    commit(status);
                                    CONN_SQLS.remove(status);
                                    iterator.remove();
                                } else if (status.isRollbackOnly()) {
                                    //回滚了
                                    CONN_SQLS.remove(status);
                                    iterator.remove();
                                }
                                //else {
                                //    log.info("else status : {}", status);
                                //}
                            }
                        } catch (Exception e) {
                            log.error("处理活跃事务异常", e);
                        }
                    } else {
                        Thread.sleep(100);
                    }
                }
            }
        }.start();
    }

    private void commit(TransactionStatus status) {
        List<String> sqls = buildSqls(status, null, null);
        sendSql2DB(sqls);
    }

    private void commit(String sql) {
        List<String> sqls = Arrays.asList(sql);
        sendSql2DB(sqls);
    }

    private void sendSql2DB(List<String> sqls) {
        if (CollectionUtils.isEmpty(sqls)) {
            return;
        }

        //todo send sqls to mq
        String json = JSON.toJSONString(sqls);
        json = json.replaceAll("'","~~~");
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            Statement statement = connection.createStatement();
            StringBuilder sb = new StringBuilder("INSERT INTO SYNC_SQL_LOG(SQLS,CREATED_AT,DELETED) values('");
            sb.append(json).append("',sysdate,0)");
            boolean execute = statement.execute(sb.toString());
            log.info(" sending sqls to db : {} , execute : {}", json, execute);
        } catch (SQLException e) {
            log.error("存储sql异常", e);
        }
    }

    @Override
    public void remove() {
        TRANSACTION_STATUS.remove();
    }
}
