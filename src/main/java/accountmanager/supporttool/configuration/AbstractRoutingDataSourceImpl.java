package accountmanager.supporttool.configuration;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import javax.sql.DataSource;
import java.util.Map;

public class AbstractRoutingDataSourceImpl extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> DATABASE_NAME = new ThreadLocal<>();

    public AbstractRoutingDataSourceImpl(DataSource defaultTargetDatasource, Map<Object,Object> targetDatasources) {
        super.setDefaultTargetDataSource(defaultTargetDatasource);
        super.setTargetDataSources(targetDatasources);
        super.afterPropertiesSet();
    }
    public static void setDatabaseName(String key) {DATABASE_NAME.set(key);}

    public static String getDatabaseName() {
        return DATABASE_NAME.get();
    }

    public static void removeDatabaseName() {
        DATABASE_NAME.remove();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DATABASE_NAME.get();
    }
}
