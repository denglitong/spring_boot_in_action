package com.denglitong.readinglist.controller;

import com.denglitong.readinglist.config.AmazonProperties;
import com.denglitong.readinglist.entity.BookEntity;
import com.denglitong.readinglist.repository.ReadingListRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author litong.deng@foxmail.com
 * @date 2021/10/12
 */
@Controller
@RequestMapping("/readingList")
public class ReadingListController {

    private ReadingListRepository readingListRepository;
    private AmazonProperties amazonProperties;
    /**
     * 自定义 actuator metrics (Counter, Gauge, Timer, Distribution summary)
     * https://github.com/TFdream/blog/issues/340
     */
    private final Counter bookSaveCounter;

    public ReadingListController(MeterRegistry registry) {
        bookSaveCounter = registry.counter("books.saved");
    }

    @Autowired
    public void setReadingListRepository(ReadingListRepository readingListRepository) {
        this.readingListRepository = readingListRepository;
    }

    @Autowired
    public void setAmazonProperties(AmazonProperties amazonProperties) {
        this.amazonProperties = amazonProperties;
    }

    @RequestMapping(value = "/{reader}", method = GET)
    public String readersBooks(@PathVariable("reader") String reader, Model model) {
        List<BookEntity> readingList = readingListRepository.findByReader(reader);
        if (!CollectionUtils.isEmpty(readingList)) {
            model.addAttribute("books", readingList);
            model.addAttribute("amazonId", amazonProperties.getAssociatedId());
        }
        return "readingList";
    }

    @RequestMapping(value = "/{reader}", method = POST)
    public String addToReadingList(@PathVariable("reader") String reader, BookEntity book) {
        book.setReader(reader);
        readingListRepository.save(book);
        bookSaveCounter.increment();
        System.out.println("books.saved: " + bookSaveCounter.count());
        return "redirect:/readingList/{reader}";
    }
}
