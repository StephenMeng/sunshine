package team.stephen.sunshine.model.book;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "sunshine_book")
public class Book {
    @Id
    @Column(name = "alt")
    private String alt;
    @Column(name = "title")
    private String title;
    @Column(name = "author")
    private String author;
    @Column(name = "binding")
    private String binding;
    @Column(name = "catalog")
    private String catalog;
    @Column(name = "id")
    private String id;
    @Column(name = "image")
    private String image;
    @Column(name = "isbn10")
    private String isbn10;
    @Column(name = "isbn13")
    private String isbn13;
    @Column(name = "pages")
    private String pages;
    @Column(name = "price")
    private String price;
    @Column(name = "pubdate")
    private String pubdate;
    @Column(name = "publisher")
    private String publisher;
    @Column(name = "subtitle")
    private String subtitle;
    @Column(name = "summary")
    private String summary;
    @Column(name = "tags")
    private String tags;
    @Column(name = "translator")
    private String translator;
    @Column(name = "url")
    private String url;
}
