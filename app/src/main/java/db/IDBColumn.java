package db;

/**
 * Created by Leonid Kabanen on 13.07.15.
 * <p/>
 * Column of DB table
 */
public interface IDBColumn {

    /**
     * @return column name
     */
    String name();

    /**
     * @return column type (INTEGER, STRING etc.)
     */
    String type();

}
