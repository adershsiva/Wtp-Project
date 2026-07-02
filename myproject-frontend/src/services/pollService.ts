import api from '../api/axios';
import type { PollDTO } from '../types';

export const pollService = {
  getAllPolls: async (userId?: number) => {
    const response = await api.get<PollDTO[]>('/polls', {
      params: { userId }
    });
    return response.data;
  },
  getPoll: async (id: number) => {
    const response = await api.get<PollDTO>(`/polls/${id}`);
    return response.data;
  },
  createPoll: async (poll: PollDTO, userId: number) => {
    const response = await api.post<PollDTO>('/polls', poll, {
      params: { userId },
    });
    return response.data;
  },
  updatePoll: async (id: number, poll: PollDTO, userId: number) => {
    const response = await api.put<PollDTO>(`/polls/${id}`, poll, {
      params: { userId }
    });
    return response.data;
  },
  inviteUser: async (pollId: number, username: string, userId: number) => {
    const response = await api.post<PollDTO>(`/polls/${pollId}/invite`, null, {
      params: { username, userId }
    });
    return response.data;
  },
  finishPoll: async (id: number, userId: number) => {
    const response = await api.patch<PollDTO>(`/polls/${id}/finish`, null, {
      params: { userId }
    });
    return response.data;
  },
  pausePoll: async (id: number, userId: number) => {
    const response = await api.patch<PollDTO>(`/polls/${id}/pause`, null, {
      params: { userId }
    });
    return response.data;
  },
  resumePoll: async (id: number, userId: number) => {
    const response = await api.patch<PollDTO>(`/polls/${id}/resume`, null, {
      params: { userId }
    });
    return response.data;
  },
  deletePoll: async (id: number, userId: number) => {
    await api.delete(`/polls/${id}`, {
      params: { userId }
    });
  },
};
