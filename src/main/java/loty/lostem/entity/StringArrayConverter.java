package loty.lostem.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;

@Converter
public class StringArrayConverter implements AttributeConverter<List<String>, String> {

    private static final String SPLIT_CHAR = ", ";
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        //return attribute.stream().map(String::valueOf).collect(Collectors.joining(SPLIT_CHAR));
        /*try {
            return mapper.writeValueAsString(attribute); // 리스트 형식으로 저장
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }*/
        return String.join(SPLIT_CHAR, attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        /*return Arrays.stream(dbData.split(SPLIT_CHAR))
                .map(String)
                .collect(Collectors.toList());*/
        /*try {
            return mapper.readValue(dbData, List.class); // 리스트 형식으로 db에서 꺼내기
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }*/
        return Arrays.asList(dbData.split(SPLIT_CHAR));
    }
}
