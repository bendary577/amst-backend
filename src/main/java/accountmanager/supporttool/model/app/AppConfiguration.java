package accountmanager.supporttool.model.app;

import jakarta.persistence.*;

@Entity
@Table(name = "AppConfiguration")
public class AppConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "isReadOnly")
    private boolean isReadOnly;

    @Column(name = "isSchedulerEnabled")
    private boolean isSchedulerEnabled;

    public AppConfiguration(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean readOnly) {
        isReadOnly = readOnly;
    }

    public boolean isSchedulerEnabled() {
        return isSchedulerEnabled;
    }

    public void setSchedulerEnabled(boolean schedulerEnabled) {
        isSchedulerEnabled = schedulerEnabled;
    }
}
