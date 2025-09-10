package co.com.bancolombia.r2dbc.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostgresqlConnectionPropertiesTest {

    @Test
    void testConstructorAndGetters() {
        // Given
        String host = "localhost";
        Integer port = 5432;
        String database = "testdb";
        String schema = "public";
        String username = "user";
        String password = "pass";
        String options = "ssl=true";

        // When
        PostgresqlConnectionProperties properties = new PostgresqlConnectionProperties(
                host, port, database, schema, username, password, options);

        // Then
        assertEquals(host, properties.host());
        assertEquals(port, properties.port());
        assertEquals(database, properties.database());
        assertEquals(schema, properties.schema());
        assertEquals(username, properties.username());
        assertEquals(password, properties.password());
        assertEquals(options, properties.options());
    }

    @Test
    void testEqualsAndHashCode() {
        PostgresqlConnectionProperties props1 = new PostgresqlConnectionProperties(
                "localhost", 5432, "testdb", "public", "user", "pass", "ssl=true");
        PostgresqlConnectionProperties props2 = new PostgresqlConnectionProperties(
                "localhost", 5432, "testdb", "public", "user", "pass", "ssl=true");
        PostgresqlConnectionProperties props3 = new PostgresqlConnectionProperties(
                "remote", 5432, "testdb", "public", "user", "pass", "ssl=true");

        assertEquals(props1, props2);
        assertNotEquals(props1, props3);
        assertEquals(props1.hashCode(), props2.hashCode());
        assertNotEquals(props1.hashCode(), props3.hashCode());
    }

    @Test
    void testToString() {
        PostgresqlConnectionProperties properties = new PostgresqlConnectionProperties(
                "localhost", 5432, "testdb", "public", "user", "pass", "ssl=true");

        String toString = properties.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("localhost"));
        assertTrue(toString.contains("5432"));
        assertTrue(toString.contains("testdb"));
    }
}
