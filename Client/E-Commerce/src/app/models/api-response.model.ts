export interface ApiResponse<T = any> {
    statusCode: number;
    meta?: any;
    succeeded: boolean;
    message: string;
    data: T;
    errors: any;
}
