package com.example.userdataprocessingstorage.parser;

import com.example.userdataprocessingstorage.enums.FileType;
import com.example.userdataprocessingstorage.dto.request.UserInput;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvUserFileParser implements UserFileParser{
    @Override
    public FileType fileSupports() {
        return FileType.CSV;
    }

    @Override
    public List<UserInput> parse(InputStream inputStream) throws Exception {
        var out = new ArrayList<UserInput>();

        try (var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .builder()
                    .setTrim(true)
                    .setIgnoreEmptyLines(true)
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .build()
                    .parse(reader);

            for (CSVRecord record: records) {
                String name = null, email = null;

                try {
                    name = record.get("name");
                    email = record.get("email");
                } catch (IllegalArgumentException e) {
                    if (record.size() >= 1) name = record.get(0);
                    if (record.size() >= 2) email = record.get(1);
                }

                if ((name == null || name.isBlank()) && (email == null || email.isBlank())) continue;
                out.add(new UserInput(name, email));
            }
        }
        return out;
    }
}
