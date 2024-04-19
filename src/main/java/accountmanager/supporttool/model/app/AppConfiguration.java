package accountmanager.supporttool.model.app;

import jakarta.persistence.*;

@Entity
@Table(name = "AppConfiguration")
public class AppConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "isBlocked")
    private boolean isReadOnly;

    @Column(name = "isBlocked")
    private boolean isSchedulerEnabled;

    public AppConfiguration(){}



}
