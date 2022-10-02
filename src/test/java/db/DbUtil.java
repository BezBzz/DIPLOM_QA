package db;

import db.dto.CreditDto;
import db.dto.OrderDto;
import db.dto.PayDto;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class DbUtil {
    private static QueryRunner r = new QueryRunner();
    private static Connection c = getConnection();

    @SneakyThrows
    private static Connection getConnection() {
        //return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
        //return DriverManager.getConnection("jdbc:postgresql://localhost:3306/app", "app", "pass");
        return DriverManager.getConnection(System.getProperty("db.url"), "app", "pass");
    }
    @SneakyThrows
    public static void prepareDB() {
        r.update(c, "DELETE FROM order_entity;");
        r.update(c, "DELETE FROM payment_entity;");
        r.update(c, "DELETE FROM credit_request_entity;");
    }

    @SneakyThrows
    public static List<CreditDto> getCreditQueryResult() {
        return r.query(c, "SELECT * FROM credit_request_entity;", new BeanListHandler<>(CreditDto.class));
    }

    @SneakyThrows
    public static List<PayDto> getPayQueryResult() {
        return r.query(c, "SELECT * FROM payment_entity;", new BeanListHandler<>(PayDto.class));
    }

    @SneakyThrows
    public static List<OrderDto> getOrderQueryResult() {
        return r.query(c, "SELECT * FROM order_entity;", new BeanListHandler<>(OrderDto.class));
    }

}
