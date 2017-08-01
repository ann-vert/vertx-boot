package xuan.wen.zhi.qin.vertx.daos;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoryDao extends SqlDao {
    private static final Logger logger = LoggerFactory.getLogger(CategoryDao.class);

    public void query(Handler<List<JsonObject>> event, HttpServerResponse response) {
        Optional.of(response).ifPresent(consumer -> {
            super.query("SELECT *  FROM category", fn -> {
                event.handle(fn.getRows());
            }, consumer);
        });
    }

    public void save(Handler<Integer> event, HttpServerResponse response) {
        Optional.of(response).ifPresent(consumer -> {
            String sql = "INSERT INTO category(id ,version ,category_name) VALUES (? ,? ,?)";
            JsonArray params = new JsonArray().add(11).add(1).add("good");
            super.updateWithParams(sql, params, fn -> {
                event.handle(fn.getKeys().getInteger(0));
            }, consumer);
        });
    }

    public void pager(Handler<JsonObject> event, HttpServerResponse response) {
        super.getConnection(connection -> {
            super.query(connection, "SELECT *  FROM category", result -> {
                JsonObject json = new JsonObject().put("list", result.getRows());
                super.query(connection, "SELECT COUNT(*) AS total  FROM category", handler -> {
                    json.put("total", handler.getRows().get(0).getLong("total"));
                    event.handle(json);
                }, response);
            }, response);
        },  response);
    }
}