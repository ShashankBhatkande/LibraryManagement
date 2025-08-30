package com.librarysystem.book.dto;

public record BookUpdateRequest(String author, String genre, String title, Integer quantity, String imageUrl) {

}
