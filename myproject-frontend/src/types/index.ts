export const QuestionType = {
  PLAIN_TEXT: 'PLAIN_TEXT',
  BOOLEAN: 'BOOLEAN',
  NUMERIC_1_TO_5: 'NUMERIC_1_TO_5',
} as const;

export type QuestionType = (typeof QuestionType)[keyof typeof QuestionType];

export interface QuestionDTO {
  id?: number;
  content: string;
  type: QuestionType;
}

export interface PollDTO {
  id?: number;
  title: string;
  description: string;
  dueDate: string; // ISO string
  finished?: boolean;
  creatorUsername?: string;
  questions: QuestionDTO[];
  invitedUsernames?: string[];
}

export interface UserDTO {
  id?: number;
  username: string;
  email: string;
  password?: string;
}

export interface LoginRequest {
  username: string;
  password?: string;
}

export interface LoginResponse {
  token: string;
  id: number;
  username: string;
  email: string;
}

export interface AnswerDTO {
  questionId: number;
  content: string;
}

export interface ParticipationDTO {
  id?: number;
  pollId: number;
  answers: AnswerDTO[];
  username?: string; // Participant username
}

export interface QuestionResultDTO {
  questionId: number;
  content: string;
  textResponses?: string[];
  distribution?: { [key: string]: number };
  average?: number;
}

export interface PollResultDTO {
  pollId: number;
  pollTitle: string;
  questionResults: QuestionResultDTO[];
}
