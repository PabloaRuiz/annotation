package com.annotations.resource;

import com.annotations.domain.Annotation;
import com.annotations.domain.Status;
import com.annotations.repository.AnnotationRepository;
import jakarta.ws.rs.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Path("/api/v1/annotations")
public class AnnotationResource {

    public static final String GET_TITLE = "/title/{title}";
    public static final String GET_STATUS = "/completed/{completed}";
    public static final String PATCH_DESCRIPTION = "/modify/{title}";

    private final AnnotationRepository annotationRepository;

    @POST
    public void addAnnotation(Annotation annotation) {
        annotationRepository.add(annotation);
    }

    @GET()
    @Path(GET_TITLE)
    public Annotation getAnnotation(@PathParam("title") String title) {
      return annotationRepository.getTitleAnnotation(title);
    }

    @GET
    @Path(GET_STATUS)
    public List<Annotation> getListAnnotation(@PathParam("completed") String completed) {
        return annotationRepository.getAnnotations(completed);
    }

    @PATCH
    @Path(PATCH_DESCRIPTION)
    public Annotation modifyDescription(@PathParam("title") String title, String newDescription) {
        return annotationRepository.setDescriptionModify(title, newDescription);
    }
}
