package co.com.gsdd.testprocess;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;

import co.com.gsdd.testprocess.constants.GeneralConstants;
import co.com.gsdd.testprocess.dto.ProcessDto;
import co.com.gsdd.testprocess.enums.ProcessEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessList {

    public static void main(String[] args) throws IOException {
        Process pr = Runtime.getRuntime().exec(GeneralConstants.TASK_WIN);
        StringWriter writer = new StringWriter();
        IOUtils.copy(pr.getInputStream(), writer, Charset.defaultCharset());
        String winProcess = writer.toString();
        Stream.of(ProcessEnum.values())
                .forEach(pe -> getProcess(winProcess, pe.name().toLowerCase() + GeneralConstants.EXE).ifPresentOrElse(
                        p -> log.info("{} {} {} {} {}", p.getName(), p.getPid(), p.getSesion(), p.getSesionId(),
                                p.getMemory()),
                        () -> log.error("Process: {} is not in execution.", pe.name().toLowerCase())));
    }

    /**
     * Get all process on execution by name.
     * 
     * @param in
     *            inputstream.
     * @param filter
     *            process to search.
     * @return process found
     */
    public static Optional<ProcessDto> getProcess(String in, String filter) {
        ProcessDto temp = null;
        in = in.substring(in.lastIndexOf(GeneralConstants.INDEX_SEP) + 1);
        String[] array = in.split(GeneralConstants.KB);

        if (array != null && filter != null) {
            for (String s : array) {
                s = s.trim();
                s = s.replaceAll(GeneralConstants.REGEX_SPACE, GeneralConstants.SPLIT);
                String[] aux = s.split(GeneralConstants.SPLIT);
                if (aux != null && aux.length == 5) {
                    if (aux[0].trim().equals(filter.trim())) {
                        temp = new ProcessDto();
                        temp.setName(aux[0]);
                        temp.setPid(aux[1]);
                        temp.setSesion(aux[2]);
                        temp.setSesionId(aux[3]);
                        temp.setMemory(aux[4] + GeneralConstants.KB);
                    }
                }
            }
        }
        return Optional.ofNullable(temp);
    }

}
