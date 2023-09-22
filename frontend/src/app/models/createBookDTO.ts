export interface CreateBookDTO {
    title: string,
    isbn: string,
    author: string,
    coverImage: string,
    description: string,
    categoryIds: number[]
}