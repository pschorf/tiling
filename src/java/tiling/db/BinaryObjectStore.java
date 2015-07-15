package tiling.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BinaryObjectStore {

    private static final String QUERY = "INSERT INTO blobs (content) VALUES (?)";

    public static int store(Connection conn, InputStream data, int length) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(QUERY, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setBinaryStream(1, data, length);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new IllegalStateException("No key returned");
            }
        }
    }
}
