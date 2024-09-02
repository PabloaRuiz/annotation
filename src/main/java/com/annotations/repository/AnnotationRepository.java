package com.annotations.repository;

import com.annotations.domain.Annotation;
import com.annotations.exceptions.AnnotationNotFoundException;
import com.annotations.exceptions.ExceptionManager;
import com.annotations.exceptions.InvalidArgumentException;
import com.annotations.infra.MongoConnection;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.Updates;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.annotations.domain.Annotation.*;
import static com.annotations.domain.Status.OPEN;
import static com.annotations.exceptions.ErrorMessages.*;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static java.util.Optional.ofNullable;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class AnnotationRepository {

    private final MongoConnection mongoConnection;

    private final ObjectMapper mapper;

    public void add(Annotation annotation) {
        if (annotation == null) {
            log.warn("Unable to persist the JSON: {} ", annotation);
            throw new InvalidArgumentException(ILLEGAL_ARGUMENT.getMessage());
        }
        mongoConnection.getCollection()
                .insertOne(annotationToDocument(annotation));
    }

    public Annotation getIdAnnotation(String id) {
        return ofNullable(mongoConnection.getCollection()
                        .find(and(
                                eq(ID_ANNOTATION, id),
                                eq(STATUS, OPEN)
                        )).first()
                )
                .map(Annotation::documentToAnnotation)
                .orElseThrow(() -> new AnnotationNotFoundException(NOT_FOUND_ANNOTATION.getMessage()));
    }

    public List<Annotation> getAnnotations(String status) {
        if (status != null) {
            var filter = eq(STATUS, status);

            var annotations = mongoConnection.getCollection()
                    .find(filter)
                    .map(Annotation::documentToAnnotation)
                    .into(new ArrayList<>());

            if (annotations.isEmpty()) {
                log.warn("The list annotations using STATUS: {} is empty", status);
                throw new AnnotationNotFoundException(NOT_FOUND_ANNOTATION.getMessage());
            }
            return annotations;
        }
        throw new InvalidArgumentException(ILLEGAL_ARGUMENT.getMessage());
    }

    public Annotation setDescriptionModify(String id, String jsonBody) {
        if (id != null || jsonBody != null) {
            var filter = eq(ID_ANNOTATION, id);
            var update = Updates.set(DESCRIPTION, extractDescription(jsonBody));
            mongoConnection.getCollection()
                    .updateOne(filter, update);
            return getIdAnnotation(id);
        }

        throw new InvalidArgumentException(ILLEGAL_ARGUMENT.getMessage());
    }

    private String extractDescription(String jsonBody) {
        var newDescription = "";
        try {
            JsonNode jsonNode = mapper.readTree(jsonBody);
            newDescription = jsonNode.get(DESCRIPTION).asText();
        } catch (Exception e) {
            log.warn("It was not possible to deserialize the JSON: {}. " +
                    "The following error occurred: {}", jsonBody, e.getMessage());
            throw new ExceptionManager(ERROR_JSON.getMessage());
        }
        return newDescription;
    }

}
