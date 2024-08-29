package com.annotations.repository;

import com.annotations.domain.Annotation;
import com.annotations.domain.Status;
import com.annotations.exceptions.AnnotationNotFoundException;
import com.annotations.exceptions.ErrorMessages;
import com.annotations.exceptions.ExceptionManager;
import com.annotations.infra.MongoConnection;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.Updates;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.annotations.domain.Annotation.*;
import static com.annotations.domain.Status.FINISHED;
import static com.annotations.domain.Status.OPEN;
import static com.annotations.exceptions.ErrorMessages.ERROR_JSON;
import static com.annotations.exceptions.ErrorMessages.NOT_FOUND_ANNOTATION;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@RequiredArgsConstructor
@ApplicationScoped
public class AnnotationRepository {

    private final MongoConnection mongoConnection;

    private final ObjectMapper mapper;

    public void add(Annotation annotation) {
        mongoConnection.getCollection()
                .insertOne(annotationToDocument(annotation));
    }

    public Annotation getTitleAnnotation(String title) {
        return Optional.ofNullable(mongoConnection.getCollection()
                        .find(and(
                                eq(TITLE, title),
                                eq(STATUS, OPEN)
                        )).first()
                )
                .map(Annotation::documentToAnnotation)
                .orElseThrow(() -> new AnnotationNotFoundException(NOT_FOUND_ANNOTATION.getMessage()));
    }

    public List<Annotation> getAnnotations(String status) {
        var filter = eq(status, FINISHED);

        var annotations = new ArrayList<Annotation>();

        for (Document document : mongoConnection.getCollection().find(filter)) {
            annotations.add(documentToAnnotation(document));
        }

        return annotations;
    }

    public Annotation setDescriptionModify(String tittle, String jsonBody) {
        var filter = eq(TITLE, tittle);
        var update = Updates.set(DESCRIPTION, extractDescription(jsonBody));
        mongoConnection.getCollection()
                .updateOne(filter, update);

        return getTitleAnnotation(tittle);
    }

    private String extractDescription(String jsonBody) {
        var newDescription = "";
        try {
            JsonNode jsonNode = mapper.readTree(jsonBody);
            newDescription = jsonNode.get(DESCRIPTION).asText();
        } catch (Exception e) {
            throw new ExceptionManager(ERROR_JSON.getMessage());
        }
        return newDescription;
    }
}
