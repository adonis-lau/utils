package bid.adonis.lau.mysql;

import bid.adonis.lau.utils.Database;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author: Adonis Lau
 * @email: adonis.lau.dev@gmail.com
 * @date: 2018/5/17 23:48
 */
public class LoadData {

    public static InputStream getTestDataInputStream() {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= 10; i++) {
            for (int j = 0; j <= 10000; j++) {

                builder.append(4);
                builder.append("\t");
                builder.append(4 + 1);
                builder.append("\t");
                builder.append(4 + 2);
                builder.append("\t");
                builder.append(4 + 3);
                builder.append("\t");
                builder.append(4 + 4);
                builder.append("\t");
                builder.append(4 + 5);
                builder.append("\n");
            }
        }
//        System.out.println(builder.toString());
        byte[] bytes = builder.toString().getBytes();
        InputStream is = new ByteArrayInputStream(bytes);
        return is;
    }

    /**
     * load bulk data from InputStream to MySQL
     */
    public int bulkLoadFromInputStream(String loadDataSql, InputStream dataStream) throws SQLException, IOException {
        if (dataStream == null) {
            System.out.println("InputStream is null ,No data is imported");
            return 0;
        }
        Database database = new Database();
        Connection conn = database.getConnection();
        PreparedStatement statement = conn.prepareStatement(loadDataSql);

        int result = 0;

        if (statement.isWrapperFor(com.mysql.jdbc.Statement.class)) {

            com.mysql.jdbc.PreparedStatement mysqlStatement = statement
                    .unwrap(com.mysql.jdbc.PreparedStatement.class);

            mysqlStatement.setLocalInfileInputStream(dataStream);
            result = mysqlStatement.executeUpdate();
        }
        database.commit(conn);
        database.close(conn);
        return result;
    }

    public static void main(String[] args) {
        String testSql = "LOAD DATA LOCAL INFILE 'sql.csv' IGNORE INTO TABLE adonis_test.load_file (a,b,c,d,e,f)";
        InputStream dataStream = getTestDataInputStream();
        LoadData dao = new LoadData();
        try {
            long beginTime = System.currentTimeMillis();
            int rows = dao.bulkLoadFromInputStream(testSql, dataStream);
            long endTime = System.currentTimeMillis();
            System.out.println("importing " + rows + " rows data into mysql and cost " + (endTime - beginTime) + " ms!");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        System.exit(1);
    }

}
