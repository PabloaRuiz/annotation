package com.annotations.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

import static com.annotations.domain.Status.OPEN;
import static com.annotations.domain.Status.valueOf;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Annotation {

    public static final String ID_ANNOTATION = "Id";
    public static final String TITLE = "title";
    public static final String STATUS = "status";
    public static final String DESCRIPTION = "description";
    public static final String DATE = "date";

    private ObjectId id;
    private String title;
    private Date date;
    private String description;
    private Status status = OPEN;


    public static Annotation documentToAnnotation(Document document) {
        return Annotation.builder()
                .title(document.getString(TITLE))
                .date(document.getDate(DATE))
                .description(document.getString(DESCRIPTION))
                .status(valueOf(document.getString(STATUS)))
                .build();
    }

    public static Document annotationToDocument(Annotation annotation) {
        return new Document()
                .append(TITLE, annotation.getTitle())
                .append(DATE, annotation.getDate())
                .append(DESCRIPTION, annotation.getDescription())
                .append(STATUS, annotation.getStatus());
    }
}
