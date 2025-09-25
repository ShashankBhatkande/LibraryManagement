export interface BorrowRecord {
    id: number;
    title: string;
    borrowStatus: string;
    fine: number;
    finePaid: boolean;
    borrowDate: Date;
    dueDate: Date;
    returnDate: Date;
}