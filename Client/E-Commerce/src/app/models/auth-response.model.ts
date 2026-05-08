export interface AuthResponse {
    id: number;
    token: string;
    name: string;
    username?: string;
    email: string;
    phone: string;
}
