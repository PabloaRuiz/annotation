package com.annotations.resource;

import com.annotations.domain.Annotation;
import com.annotations.repository.AnnotationRepository;
import jakarta.ws.rs.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.annotations.domain.Annotation.*;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Path("/api/v1/annotations")
public class AnnotationResource {

    public static final String GET_TITLE = "/id/{id}";
    public static final String GET_STATUS = "/status/{status}";
    public static final String PATCH_DESCRIPTION = "/modify/{id}";

    private final AnnotationRepository annotationRepository;

    @POST
    public void addAnnotation(Annotation annotation) {
        annotationRepository.add(annotation);
    }

    @GET()
    @Path(GET_TITLE)
    public Annotation getAnnotation(@PathParam(ID_ANNOTATION) String title) {
      return annotationRepository.getIdAnnotation(title);
    }

    @GET
    @Path(GET_STATUS)
    public List<Annotation> getListAnnotation(@PathParam(STATUS) String status) {
        return annotationRepository.getAnnotations(status);
    }

    @PATCH
    @Path(PATCH_DESCRIPTION)
    public Annotation modifyDescription(@PathParam(ID_ANNOTATION) String title, String newDescription) {
        return annotationRepository.setDescriptionModify(title, newDescription);
    }
}
