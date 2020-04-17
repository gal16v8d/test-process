package co.com.gsdd.testprocess.dto;

import java.io.Serializable;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

@Generated
@Setter
@Getter
public class ProcessDto implements Serializable {

    private static final long serialVersionUID = 6235826557390781553L;
    private String name;
    private String pid;
    private String sesion;
    private String sesionId;
    private String memory;

}
