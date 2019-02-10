package sk.vildibald.polls.mapping

import org.springframework.core.convert.converter.Converter
import org.springframework.data.domain.Page
import sk.vildibald.polls.payload.PagedResponse

//class PageConverter<D, R>(private val convertedContent: List<R>)
//    : Converter<Page<D>, PagedResponse<R>> {
//    override fun convert(source: Page<D>)
//            : PagedResponse<R>? {
//        return PagedResponse(
//                content = convertedContent,
//                page = source.number,
//                size = source.size,
//                totalElements = source.totalElements,
//                totalPages = source.totalElements,
//                last = source.isLast
//        )
//    }
//
//}

class PageConverter<D, R>(private val contentConverter: Converter<D, R>)
    : Converter<Page<D>, PagedResponse<R>> {
    override fun convert(source: Page<D>)
            : PagedResponse<R> {
        return PagedResponse(
                content = source.content.map { contentConverter.convert(it)!! },
                page = source.number,
                size = source.size,
                totalElements = source.totalElements,
                totalPages = source.totalElements,
                last = source.isLast
        )
    }

    fun convert(source: Page<D>, convertedContent: List<R>)
            : PagedResponse<R> {
        return PagedResponse(
                content = convertedContent,
                page = source.number,
                size = source.size,
                totalElements = source.totalElements,
                totalPages = source.totalElements,
                last = source.isLast
        )
    }
}

fun <D, R> Page<D>.toEmptyPagedResponse(): PagedResponse<R> =
        PagedResponse(
                content = emptyList(),
                page = this.number,
                size = this.size,
                totalElements = this.totalElements,
                totalPages = this.totalElements,
                last = this.isLast
        )