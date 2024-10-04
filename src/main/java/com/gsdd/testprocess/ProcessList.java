package com.gsdd.testprocess;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;
import com.gsdd.testprocess.constants.GeneralConstants;
import com.gsdd.testprocess.dto.ProcessDto;
import com.gsdd.testprocess.enums.ProcessEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessList {

  public static void main(String[] args) throws IOException {
    var osName = System.getProperty(GeneralConstants.OS_NAME).toLowerCase();
    var command = new String[] {getCommandByOs(osName)};
    var pr = Runtime.getRuntime().exec(command);
    var writer = new StringWriter();
    IOUtils.copy(pr.getInputStream(), writer, Charset.defaultCharset());
    var ext = appendExt(osName);
    String winProcess = writer.toString();
    Stream.of(ProcessEnum.values())
        .forEach(
            pe -> getProcess(winProcess, pe.name().toLowerCase() + ext).ifPresentOrElse(
                p -> log.info(
                    "{} {} {} {} {}",
                    p.getName(),
                    p.getPid(),
                    p.getSession(),
                    p.getSessionId(),
                    p.getMemory()),
                () -> log.error("Process: {} is not in execution.", pe.name().toLowerCase())));
  }

  private static String getCommandByOs(String osName) {
    String result;
    if (osName.startsWith(GeneralConstants.OS_WIN)) {
      result = GeneralConstants.TASK_WIN;
    } else if (osName.startsWith(GeneralConstants.OS_MAC)) {
      result = GeneralConstants.TASK_LIN;
    } else if (osName.startsWith(GeneralConstants.OS_LIN)) {
      result = GeneralConstants.TASK_LIN;
    } else {
      result = null;
    }
    return result;
  }

  private static String appendExt(String osName) {
    return osName.startsWith(GeneralConstants.OS_WIN) ? GeneralConstants.EXE : "";
  }

  /**
   * Get all process on execution by name.
   *
   * @param in inputstream.
   * @param filter process to search.
   * @return process found
   */
  private static Optional<ProcessDto> getProcess(String in, String filter) {
    ProcessDto temp = null;
    in = in.substring(in.lastIndexOf(GeneralConstants.INDEX_SEP) + 1);
    String[] array = in.split(GeneralConstants.KB);

    if (array != null && filter != null) {
      for (String s : array) {
        s = s.trim();
        s = s.replaceAll(GeneralConstants.REGEX_SPACE, GeneralConstants.SPLIT);
        String[] aux = s.split(GeneralConstants.SPLIT);
        if (aux != null && aux.length == 5 && aux[0].trim().equals(filter.trim())) {
          temp = ProcessDto.builder()
              .name(aux[0])
              .pid(aux[1])
              .session(aux[2])
              .sessionId(aux[3])
              .memory(aux[4] + GeneralConstants.KB)
              .build();
        }
      }
    }
    return Optional.ofNullable(temp);
  }

}
