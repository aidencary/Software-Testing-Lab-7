// Matches Student.java
export interface StudentResponse {
    id: number;
    name: string;
    major: string;
    gpa: number;
}

export interface StudentRequest {
    id?: number;
    name: string;
    major: string;
    gpa: number;
}

// Matches CourseResponse (the @GetMapping return type)
export interface CourseResponse {
    id: number;
    name: string;
    instructor: string;
    maxSize: number;
    room: string;
    roster: string[]; // Set of student names
}

// Matches CourseRequest (the @PostMapping/@PutMapping input type)
export interface CourseRequest {
    name: string;
    instructor: number;
    maxSize: number;
    room: string;
    roster?: number[]; // List of Student IDs
}