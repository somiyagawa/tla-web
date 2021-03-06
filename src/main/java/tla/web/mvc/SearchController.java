package tla.web.mvc;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import tla.domain.command.LemmaSearch;
import tla.domain.model.Language;
import tla.domain.model.Script;
import tla.web.config.LemmaSearchProperties;
import tla.web.model.ui.BreadCrumb;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private LemmaSearchProperties lemmaSearchConf;

    @ModelAttribute("allScripts")
    public Script[] getAllScripts() {
        return LemmaController.SEARCHABLE_SCRIPTS;
    }

    @ModelAttribute("allTranslationLanguages")
    public Language[] getAllTranslationLanguages() {
        return LemmaController.SEARCHABLE_TRANSLATION_LANGUAGES;
    }

    @ModelAttribute("wordClasses")
    public Map<String, List<String>> getWordClasses() {
        return lemmaSearchConf.getWordClasses();
    }

    @ModelAttribute("lemmaAnnotationTypes")
    public Map<String, List<String>> getLemmaAnnotationTypes() {
        return lemmaSearchConf.getAnnotationTypes();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String mainSearchPage(
        @ModelAttribute("lemmaSearchForm") LemmaSearch form,
        Model model
    ) {
        model.addAttribute(
            "breadcrumbs",
            List.of(
                BreadCrumb.of("/", "menu_global_home"),
                BreadCrumb.of("/search", "menu_global_search")
            )
        );
        return "search";
    }

    /**
     * This is for testing obviously.
     *
     * @throws Exception
     */
    @RequestMapping(value = "fail", method = RequestMethod.GET)
    public void failWithError(Model model) throws Exception {
        throw new Exception("something went wrong!");
    }

}
