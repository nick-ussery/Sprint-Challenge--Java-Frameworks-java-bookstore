package com.lambdaschool.bookstore.services;

import com.lambdaschool.bookstore.BookstoreApplication;
import com.lambdaschool.bookstore.exceptions.ResourceNotFoundException;
import com.lambdaschool.bookstore.models.Author;
import com.lambdaschool.bookstore.models.Book;
import com.lambdaschool.bookstore.models.Section;
import com.lambdaschool.bookstore.models.Wrote;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookstoreApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//**********
// Note security is handled at the controller, hence we do not need to worry about security here!
//**********
public class BookServiceImplTest
{

    @Autowired
    private BookService bookService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private AuthorService authorService;

    @Before
    public void setUp() throws
            Exception
    {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws
            Exception
    {
    }

    @Test
    public void afindAll()
    {
        assertEquals(5, bookService.findAll().size());
    }

    @Test
    public void bfindBookById()
    {
        assertEquals("Flatterland", bookService.findBookById(26).getTitle());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void cnotFindBookById()
    {
        bookService.findBookById(1);
    }

    @Test
    public void delete()
    {
        bookService.delete(26);
        assertEquals(4, bookService.findAll().size());
    }

    @Test (expected = ResourceNotFoundException.class)
    public void dfdeleteFailTest()
    {
        bookService.delete(1);
    }

    @Test
    public void esave()
    {
        Section s1 = new Section("Testing");
        s1 = sectionService.save(s1);
        Book newBook = new Book("Lambda Sprint Challenge", "123456789", 2020, s1);
        bookService.save(newBook);

        assertEquals(5, bookService.findAll().size());
    }

    @Test
    public void fupdate()
    {
        Author a7 = new Author("Nick", "Ussery");
        a7 = authorService.save(a7);
        Section s1 = new Section("Testing");
        s1 = sectionService.save(s1);
        Set<Wrote> wrote = new HashSet<>();
        wrote.add(new Wrote(a7, new Book()));
        Book b1 = new Book("Java Update Test", "9780738206752", 2001, s1);
        b1.setWrotes(wrote);
        b1 = bookService.save(b1);

        bookService.update(b1, 27);
        assertEquals("Java Update Test", bookService.findBookById(27).getTitle());
    }

    @Test
    public void gdeleteAll()
    {
        bookService.deleteAll();
        assertEquals(0, bookService.findAll().size());
    }

    @Test (expected = ResourceNotFoundException.class)
    public void hsaveWithBadId()
    {
        Section s1 = new Section("Fail");
        s1 = sectionService.save(s1);
        Book failBook = new Book();
        failBook.setBookid(100);
        failBook= bookService.save(failBook);
    }
}