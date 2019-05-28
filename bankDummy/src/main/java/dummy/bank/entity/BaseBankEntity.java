package dummy.bank.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonView;

import dummy.security.model.User;
import dummy.security.model.UserEvent;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseBankEntity implements Serializable {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonView({UserEvent.View.DetailForCommonUser.class, BankManageEvent.View.DetailForManageBank.class})
    @CreatedDate
    private Date createTime;

    @Column(name = "create_by", nullable = false)
    @JsonView({UserEvent.View.DetailForCommonUser.class, BankManageEvent.View.DetailForManageBank.class})
    @CreatedBy
    private Long createBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonView({UserEvent.View.DetailForCommonUser.class, BankManageEvent.View.DetailForManageBank.class})
    @UpdateTimestamp
    private Date updateTime;

    @Column(name = "update_by", nullable = false)
    @JsonView({UserEvent.View.DetailForCommonUser.class, BankManageEvent.View.DetailForManageBank.class})
    private Long updateBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by", insertable = false, updatable = false)
    @JsonView({UserEvent.View.DetailForCommonUser.class, BankManageEvent.View.DetailForManageBank.class})
    private User createByUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "update_by", insertable = false, updatable = false)
    @JsonView({UserEvent.View.DetailForCommonUser.class, BankManageEvent.View.DetailForManageBank.class})
    private User updateByUser;

    @Version
    @Column(name = "version", nullable = false, columnDefinition = "tinyint(1) default 1")
    @JsonView({BankManageEvent.View.DetailForManageBank.class, UserEvent.View.DetailForCommonUser.class})
    private int version;


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public User getCreateByUser() {
        return createByUser;
    }

    public void setCreateByUser(User createByUser) {
        this.createByUser = createByUser;
    }

    public User getUpdateByUser() {
        return updateByUser;
    }

    public void setUpdateByUser(User updateByUser) {
        this.updateByUser = updateByUser;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
