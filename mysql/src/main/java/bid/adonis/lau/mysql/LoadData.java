package bid.adonis.lau.mysql;

import bid.adonis.lau.utils.Database;
import org.apache.commons.lang3.RandomStringUtils;

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
        for (int j = 0; j < 100; j++) {
//            String a = RandomStringUtils.randomAlphabetic(10).toLowerCase();
            builder.append(String.valueOf((int) (Math.random() * 100)));
            builder.append("\t");
            builder.append(RandomStringUtils.randomAlphabetic(10).toLowerCase());
            builder.append("\t");
            builder.append(String.valueOf((int) (Math.random() * 100)));
            builder.append("\t");
            builder.append(RandomStringUtils.randomAlphabetic(10).toLowerCase());
            builder.append("\t");
            builder.append(RandomStringUtils.randomAlphabetic(10).toLowerCase());
            builder.append("\t");
            builder.append(RandomStringUtils.randomAlphabetic(10).toLowerCase());
            builder.append("\n");
        }
        byte[] bytes = builder.toString().getBytes();
        return new ByteArrayInputStream(bytes);
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
        String testSql = "LOAD DATA LOCAL INFILE 'sql.csv' REPLACE INTO TABLE adonis_test.load_file FIELDS TERMINATED BY '\\t' ENCLOSED BY '\"' LINES TERMINATED BY '\\n'";
        LoadData dao = new LoadData();

        StringBuilder builder = new StringBuilder();


        builder.append(1);
        builder.append("\t");
        builder.append("\"8\"");
        builder.append("\t");
        builder.append(3);
        builder.append("\t");
        builder.append("\"9\"");
        builder.append("\t");
        builder.append("546");
        builder.append("\t");
        builder.append("\"5\"");
        builder.append("\n");

        System.out.println(builder.toString());

        byte[] bytes = builder.toString().getBytes();
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);

        try {
            int row = dao.bulkLoadFromInputStream(testSql, is);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

//        try {
//            long beginTime = System.currentTimeMillis();
//            int rows = 0;
//            for (int i = 0; i < 1000; i++) {
//                InputStream dataStream = getTestDataInputStream();
//                int row = dao.bulkLoadFromInputStream(testSql, dataStream);
//                System.out.println("第" + (i + 1) + "次插入" + row + "条");
//                rows += row;
//            }
//            long endTime = System.currentTimeMillis();
//            System.out.println("importing " + rows + " rows data into mysql and cost " + (endTime - beginTime) + " ms!");
//        } catch (SQLException | IOException e) {
//            e.printStackTrace();
//        }
        System.exit(1);
    }

}
