package tla.web.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tla.domain.model.Passport;
import tla.domain.model.meta.BTSeClass;
import tla.web.model.mappings.Util;

@Data
@NoArgsConstructor
@BTSeClass("BTSAnnotation")
@EqualsAndHashCode(callSuper = true)
public class Annotation extends TLAObject {

    @Getter(AccessLevel.NONE)
    private Collection<String> body;

    /**
     * Escape markup before returning value.
     */
    @Override
    public String getName() {
        return Util.escapeMarkup(super.getName());
    }

    /**
     * Lemma annotations have textual content in their passports
     * and we want to access that conveniently so just copy it
     * into here.
     * Might return null tho.
     * Escapes markup.
     */
    public String getBody() {
        return Util.escapeMarkup(
            String.join(
                "\n\n",
                this.body != null ? this.body : this.extractBody()
            )
        );
    }

    /**
     * Try to extract text content from <code>"annotation.lemma"</code> nodes in the annotation's passport.
     */
    private Collection<String> extractBody() {
        if (this.getPassport() != null) {
            List<Passport> nodes = this.getPassport().extractProperty(
                "annotation.lemma"
            );
            if (nodes != null) {
                return nodes.stream().filter(
                    node -> {
                        return !node.isEmpty() && !node.getLeafNodeValue().isBlank();
                    }
                ).map(
                    Passport::getLeafNodeValue
                ).map(
                    String::trim
                ).collect(
                    Collectors.toList()
                );
            }
        }
        return Collections.emptyList();
    }

}