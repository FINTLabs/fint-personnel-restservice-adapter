import no.fint.model.resource.Link
import no.fint.personnel.service.LinkMapperService
import spock.lang.Specification

class LinkMapperSpec extends Specification {
    def 'Able to map a proper link'() {
        given:
        def link = Link.with('https://alpha.felleskomponent.no/administrasjon/personal/personalressurs/systemid/300000008068001')

        when:
        def result = LinkMapperService.remapLink(link)

        then:
        result.href == '${}/administrasjon/personal/personalressurs/systemid/300000008068001'
    }

    def 'If link already mapped, should pass through'() {
        given:
        def link = Link.with('https://alpha.felleskomponent.no/administrasjon/personal/personalressurs/systemid/300000008068001')

        when:
        def result = LinkMapperService.remapLink(LinkMapperService.remapLink(link))

        then:
        result.href == '${}/administrasjon/personal/personalressurs/systemid/300000008068001'

    }
}
