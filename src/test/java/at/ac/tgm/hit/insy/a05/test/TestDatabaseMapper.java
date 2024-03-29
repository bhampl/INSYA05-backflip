package at.ac.tgm.hit.insy.a05.test;

import at.ac.tgm.hit.insy.a05.input.source.DatabaseMapper;
import at.ac.tgm.hit.insy.a05.structure.Attribute;
import at.ac.tgm.hit.insy.a05.structure.Database;
import at.ac.tgm.hit.insy.a05.structure.Table;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.*;

import static org.junit.Assert.*;

/**
 * Tests the Methods of the DatabaseMapper Class and the package structure.
 *
 * @author Martin Kritzl [mkritzl@student.tgm.ac.at]
 */
public class TestDatabaseMapper {

    private Database database;

    /**
     * Flugzeug: Nummer
     *           seats
     * Flight: nr
     *         Primary foreign airline id
     *         Attribut Flugzeug id
     * Airline: id
     *          land
     */

    @Before
    public void initialize() throws SQLException {
        //Initialize Mock Objects
        Connection con = Mockito.mock(Connection.class);
        DatabaseMetaData meta = Mockito.mock(DatabaseMetaData.class);
        ResultSetMetaData planesMetaData = Mockito.mock(ResultSetMetaData.class);
        ResultSetMetaData flightsMetaData = Mockito.mock(ResultSetMetaData.class);
        ResultSetMetaData airlinesMetaData = Mockito.mock(ResultSetMetaData.class);
        ResultSet tables = Mockito.mock(ResultSet.class);
        ResultSet columnsPlane = Mockito.mock(ResultSet.class);
        ResultSet pksPlane = Mockito.mock(ResultSet.class);
        ResultSet foreignPlane = Mockito.mock(ResultSet.class);
        ResultSet uniquePlane = Mockito.mock(ResultSet.class);
        ResultSet columnsFlight = Mockito.mock(ResultSet.class);
        ResultSet pksFlight = Mockito.mock(ResultSet.class);
        ResultSet foreignFlight = Mockito.mock(ResultSet.class);
        ResultSet uniqueFlight = Mockito.mock(ResultSet.class);
        ResultSet columnsAirline = Mockito.mock(ResultSet.class);
        ResultSet pksAirline = Mockito.mock(ResultSet.class);
        ResultSet foreignAirline = Mockito.mock(ResultSet.class);
        ResultSet uniqueAirline = Mockito.mock(ResultSet.class);


        //Set Metadatas
        Mockito.when(con.getMetaData()).thenReturn(meta);
        Mockito.when(con.getCatalog()).thenReturn("MyDatabase");
        Mockito.when(meta.getTables(null, null, "%", null)).thenReturn(tables).thenReturn(tables);

        Statement statement = Mockito.mock(Statement.class);
        ResultSet planesQuery = Mockito.mock(ResultSet.class);
        ResultSet flightsQuery = Mockito.mock(ResultSet.class);
        ResultSet airlinesQuery = Mockito.mock(ResultSet.class);

        Mockito.when(con.createStatement()).thenReturn(statement);

        Mockito.when(statement.executeQuery("SELECT * FROM planes")).thenReturn(planesQuery);
        Mockito.when(statement.executeQuery("SELECT * FROM flights")).thenReturn(flightsQuery);
        Mockito.when(statement.executeQuery("SELECT * FROM airlines")).thenReturn(airlinesQuery);

        Mockito.when(planesQuery.getMetaData()).thenReturn(planesMetaData);
        Mockito.when(flightsQuery.getMetaData()).thenReturn(flightsMetaData);
        Mockito.when(airlinesQuery.getMetaData()).thenReturn(airlinesMetaData);

//        Mockito.when(con.createStatement().executeQuery("SELECT * FROM planes").getMetaData()).thenReturn(planesMetaData);
//        Mockito.when(con.createStatement().executeQuery("SELECT * FROM flights").getMetaData()).thenReturn(flightsMetaData);
//        Mockito.when(con.createStatement().executeQuery("SELECT * FROM airlines").getMetaData()).thenReturn(airlinesMetaData);

        Mockito.when(planesMetaData.isNullable(1)).thenReturn(ResultSetMetaData.columnNoNulls);
        Mockito.when(planesMetaData.isNullable(2)).thenReturn(ResultSetMetaData.columnNullable);

        Mockito.when(flightsMetaData.isNullable(1)).thenReturn(ResultSetMetaData.columnNoNulls);
        Mockito.when(flightsMetaData.isNullable(2)).thenReturn(ResultSetMetaData.columnNullable);
        Mockito.when(flightsMetaData.isNullable(3)).thenReturn(ResultSetMetaData.columnNullable);

        Mockito.when(airlinesMetaData.isNullable(1)).thenReturn(ResultSetMetaData.columnNoNulls);
        Mockito.when(airlinesMetaData.isNullable(2)).thenReturn(ResultSetMetaData.columnNoNulls);

        //Prepare tables
        Mockito.when(tables.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false)
                                        .thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(tables.getString("TABLE_NAME")).thenReturn("planes").thenReturn("flights").thenReturn("airlines")
                                                         .thenReturn("planes").thenReturn("flights").thenReturn("airlines");

        //Prepare primary keys, attributes, foreign keys and unique Attributes
        Mockito.when(meta.getColumns(null, null, "planes", null)).thenReturn(columnsPlane);
        Mockito.when(meta.getPrimaryKeys(null, null, "planes")).thenReturn(pksPlane);
        Mockito.when(meta.getImportedKeys(null, null, "planes")).thenReturn(foreignPlane);
        Mockito.when(meta.getIndexInfo(null, null, "planes", true, true)).thenReturn(uniquePlane);
        Mockito.when(meta.getColumns(null, null, "flights", null)).thenReturn(columnsFlight);
        Mockito.when(meta.getImportedKeys(null, null, "flights")).thenReturn(foreignFlight);
        Mockito.when(meta.getIndexInfo(null, null, "flights", true, true)).thenReturn(uniqueFlight);
        Mockito.when(meta.getPrimaryKeys(null, null, "flights")).thenReturn(pksFlight);
        Mockito.when(meta.getColumns(null, null, "airlines", null)).thenReturn(columnsAirline);
        Mockito.when(meta.getPrimaryKeys(null, null, "airlines")).thenReturn(pksAirline);
        Mockito.when(meta.getImportedKeys(null, null, "airlines")).thenReturn(foreignAirline);
        Mockito.when(meta.getIndexInfo(null, null, "airlines", true, true)).thenReturn(uniqueAirline);


        //Set primary keys
        Mockito.when(pksPlane.next()).thenReturn(true).thenReturn(false);
        Mockito.when(pksPlane.getString("COLUMN_NAME")).thenReturn("id");
        Mockito.when(pksFlight.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(pksFlight.getString("COLUMN_NAME")).thenReturn("nr").thenReturn("airline");
        Mockito.when(pksAirline.next()).thenReturn(true).thenReturn(false);
        Mockito.when(pksAirline.getString("COLUMN_NAME")).thenReturn("id");

        //Set attributes
        Mockito.when(columnsPlane.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(columnsPlane.getString("COLUMN_NAME")).thenReturn("id").thenReturn("seats");
        Mockito.when(columnsFlight.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(columnsFlight.getString("COLUMN_NAME")).thenReturn("nr").thenReturn("airline").thenReturn("plane");
        Mockito.when(columnsAirline.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(columnsAirline.getString("COLUMN_NAME")).thenReturn("id").thenReturn("land");

        //Set foreign keys
        Mockito.when(foreignFlight.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(foreignFlight.getString("FKCOLUMN_NAME")).thenReturn("plane").thenReturn("airline");
        Mockito.when(foreignFlight.getString("PKTABLE_NAME")).thenReturn("planes").thenReturn("airlines");
        Mockito.when(foreignFlight.getString("PKCOLUMN_NAME")).thenReturn("id").thenReturn("id");

        //Set unique
        //All primary keys are also unique
        Mockito.when(uniquePlane.next()).thenReturn(true).thenReturn(false);
        Mockito.when(uniquePlane.getString("COLUMN_NAME")).thenReturn("id");
        Mockito.when(uniqueFlight.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        //Return "plane" although it's not a primary key
        Mockito.when(uniqueFlight.getString("COLUMN_NAME")).thenReturn("nr").thenReturn("airline").thenReturn("plane");
        Mockito.when(uniqueAirline.next()).thenReturn(true).thenReturn(false);
        Mockito.when(uniqueAirline.getString("COLUMN_NAME")).thenReturn("id");

        this.database = new DatabaseMapper(con).executeMapping();
    }

    @Test
    public void testDatabaseName() throws SQLException {
        assertEquals("MyDatabase", this.database.getName());
    }

    @Test
    public void testGetTable() throws SQLException {
        assertEquals("planes", this.database.getTable("planes").getName());
        assertEquals("flights", this.database.getTable("flights").getName());
        assertEquals("airlines", this.database.getTable("airlines").getName());
    }

    @Test
    public void testGetAttributes() throws SQLException {
        assertEquals("seats", this.database.getTable("planes").getAttribute("seats").getName());
    }

    @Test
    public void testGetPrimaryKeys() throws SQLException {
        assertEquals("id", this.database.getTable("planes").getPrimaryKey("id").getName());
    }

    @Test
    public void testGetTableWithNotEnteredName() {
        assertEquals(null, this.database.getTable("NotEnteredName"));
    }

    @Test
    public void getReferenzTable() {
        assertEquals("airlines", this.database.getTable("flights").getPrimaryKey("airline").getReference().getRefTable().getName());
    }

    @Test
    public void getReferenzAttribut() {
        assertEquals("id", this.database.getTable("flights").getPrimaryKey("airline").getReference().getRefAttribute().getName());
    }

    @Test
    public void getAttribute() {
        assertEquals("seats", this.database.getTable("planes").getAttribute("seats").getName());
    }

    @Test
    public void getNotEnteredNameAttribute() {
        assertEquals(null, this.database.getTable("planes").getAttribute("laenge"));
    }

    @Test
    public void getPrimaryKey() {
        assertEquals("id", this.database.getTable("planes").getPrimaryKey("id").getName());
    }

    @Test
    public void getNotEnteredNamePrimaryKey() {
        assertEquals(null, this.database.getTable("planes").getPrimaryKey("nr"));
    }

    @Test
    public void compareTheSameAttribute() {
        Attribute temp = new Attribute("newAttribute", new Table("temp"));
        assertTrue(temp.equals(temp));
    }

    @Test
    public void compareTwoDifferentAttributes() {
        Attribute temp = new Attribute("newAttribute", new Table("temp"));
        assertFalse(temp.equals(this.database.getTable("flights").getAttribute("plane")));
    }

    @Test
    public void compareAttributeWithNull() {
        Attribute temp = new Attribute("newAttribute", new Table("temp"));
        assertFalse(temp.equals(null));
    }

    @Test
    public void CompareAttributeWithAnotherInstance() {
        Attribute temp = new Attribute("newAttribute", new Table("temp"));
        assertFalse(temp.equals(new Table("newTable")));
    }

    @Test
    public void CompareSameAttributeWithDifferentTable() {
        Attribute temp = new Attribute("newAttribute", new Table("temp"));
        assertFalse(temp.equals(new Attribute("otherAttribute",new Table("temp"))));
    }

    @Test
    public void getAttributes() {
        assertEquals(1, this.database.getTable("planes").getAttributes().size());
    }

    @Test
    public void testUniqueAttribute() {
        assertTrue(this.database.getTable("flights").getAttribute("plane").isUnique());
    }

    @Test
    public void testNonUniqueAttribute() {
        assertFalse(this.database.getTable("planes").getAttribute("seats").isUnique());
    }

    @Test
     public void testGetTableFromAttribute() {
        assertFalse(this.database.getTable("planes").getAttribute("seats").getTable().equals("planes"));
    }

    @Test
    public void testNotNull() {
        assertTrue(this.database.getTable("airlines").getAttribute("land").isNotNull());
    }

    @Test
    public void tesIsNullable() {
        assertFalse(this.database.getTable("planes").getAttribute("seats").isNotNull());
    }
}
