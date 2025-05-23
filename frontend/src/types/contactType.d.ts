export interface Contact {
    userId: string;
    contactId: string;
    contactName: string;
    contactPhone: string;
    contactEmail: string;
    createdAt: string;
}

export interface ContactOutput {
    code: number;
    message: string;
    data: Object;
}
