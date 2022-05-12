package ru.aot.dbanalyser.fileworkers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aot.dbanalyser.deserializer.SearchDeserializer;
import ru.aot.dbanalyser.deserializer.StatDeserializer;
import ru.aot.dbanalyser.dto.input.InputFileDto;
import ru.aot.dbanalyser.enums.Mode;

import java.io.IOException;
import java.nio.file.Path;


/**
 * Стандартный читатель данных из файла. Синглтон.
 */
public class DefaultInputFileReader implements InputFileReader {
    private static final Logger log = LoggerFactory.getLogger(DefaultInputFileReader.class.getName());

    private static DefaultInputFileReader instance;
    private final Mode mode;
    private final ObjectMapper mapper = new ObjectMapper();

    private DefaultInputFileReader(Mode mode) {
        this.mode = mode;

        SimpleModule module = new SimpleModule();
        module.addDeserializer(InputFileDto.class, new SearchDeserializer());
        module.addDeserializer(InputFileDto.class, new StatDeserializer());

        mapper.registerModule(module);
        mapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);
    }

    public static DefaultInputFileReader getInstance(Mode mode) {
        if (instance == null) {
            instance = new DefaultInputFileReader(mode);
            log.info("Новый input file reader был создан.");
        } else {
            log.warn("Input file reader уже был создан до этого!");
        }
        return instance;
    }

    /**
     * Создаёт объект с входными данными из файла.
     * @param filePath путь к файлу для чтения
     * @return объект с данными
     * @throws IOException в случае невозможности прочитать объект или в случае поврежденных/неправильных данных.
     */
    @Override
    public InputFileDto generateInputDto(Path filePath) throws IOException {

        InputFileDto inputFileDto = mapper.readValue(filePath.toFile(), InputFileDto.class);

        log.info("Входной файл успешно прочитан.");

        return inputFileDto;
    }
}
