import { Book } from "./book";
import { User } from "./user";

export interface ReviewInfo {
    id: number;
    book: Book;
    rating: number;
    comment: string;
    user: User;
}