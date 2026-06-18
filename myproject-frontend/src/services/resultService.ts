import api from '../api/axios';
import type { PollResultDTO, ParticipationDTO } from '../types';

export const resultService = {
  getAggregateResults: async (pollId: number, userId: number) => {
    const response = await api.get<PollResultDTO>(`/results/poll/${pollId}/aggregate`, {
      params: { userId }
    });
    return response.data;
  },
  getIndividualResponses: async (pollId: number, userId: number) => {
    const response = await api.get<ParticipationDTO[]>(`/results/poll/${pollId}/individual`, {
      params: { userId }
    });
    return response.data;
  },
};
