package joys.world.pointmagingProject.point;

import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
@Getter
public abstract class IdEntity implements Serializable {
    @Id
    @GeneratedValue
    Long id;
}
