package ru.aot.dbanalyser.fileworkers;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aot.dbanalyser.dto.output.DefaultResultDto;
import ru.aot.dbanalyser.dto.output.error.ErrorDtoDefault;
import ru.aot.dbanalyser.dto.output.search.SearchResultDto;
import ru.aot.dbanalyser.dto.output.stat.StatResultDto;
import ru.aot.dbanalyser.seializer.ErrorSerializer;
import ru.aot.dbanalyser.seializer.SearchSerializer;
import ru.aot.dbanalyser.seializer.StatSerializer;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Стандартный писатель данных в файл. Синглтон.
 */
public class DefaultOutputFileWriter implements OutputFileWriter {
    private static final Logger log = LoggerFactory.getLogger(DefaultOutputFileWriter.class.getName());

    private static DefaultOutputFileWriter instance;

    private final Path savePath;
    private final ObjectMapper mapper = new ObjectMapper();

    private DefaultOutputFileWriter(Path savePath) {
        this.savePath = savePath;

        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        mapper.setDefaultPrettyPrinter(pp);

        SimpleModule serializerModule = new SimpleModule();
        serializerModule.addSerializer(StatResultDto.class, new StatSerializer());
        serializerModule.addSerializer(SearchResultDto.class, new SearchSerializer());
        serializerModule.addSerializer(ErrorDtoDefault.class, new ErrorSerializer());
        mapper.registerModule(serializerModule);
    }

    public static DefaultOutputFileWriter getInstance(Path savePath) {
        if (instance == null) {
            instance = new DefaultOutputFileWriter(savePath);
            log.info("Новый output writer создан.");
        } else {
            log.warn("Output writer уже был создан до этого!");
        }
        return instance;
    }

    /**
     * Записывает данные в выходной файл.
     * @param data объект с данными для записи
     * @throws IOException в случае невозможности записать в файл
     */
    @Override
    public void writeFile(DefaultResultDto data) throws IOException {
        if (savePath != null) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(savePath.toFile(), data);

            log.info("Выходной файл записан!");
        }
    }
}
