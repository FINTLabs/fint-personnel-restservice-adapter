package no.fint.personnel.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.FintMainObject;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.Link;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.stream.Collectors;

@Slf4j
public class LinkMapperService {
    public static <T extends FintMainObject & FintLinks> T remap(T t) {
        t.getLinks().forEach((key, links) -> t.getLinks().replace(key, links.stream().map(LinkMapperService::remapLink).collect(Collectors.toList())));
        return t;
    }

    public static Link remapLink(Link link) {
        if (StringUtils.startsWith(link.getHref(), "${")) {
            return link;
        }
        final Link newLink = Link.with("${}" + URI.create(link.getHref()).getPath());
        log.trace("Remapping {} to {}", link, newLink);
        return newLink;
    }

}