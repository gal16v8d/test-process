package com.gsdd.testprocess.dto;

import java.io.Serializable;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;

@Generated
@Builder
@Getter
public class ProcessDto implements Serializable {

  private static final long serialVersionUID = 6235826557390781553L;
  private String name;
  private String pid;
  private String session;
  private String sessionId;
  private String memory;

}
