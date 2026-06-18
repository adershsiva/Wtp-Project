import api from '../api/axios';
import type { ParticipationDTO, PollDTO } from '../types';

export const participationService = {
  participate: async (participation: ParticipationDTO, userId: number) => {
    const response = await api.post<ParticipationDTO>('/participations', participation, {
      params: { userId },
    });
    return response.data;
  },
  getPendingPolls: async (userId: number) => {
    const response = await api.get<PollDTO[]>('/participations/pending', {
      params: { userId }
    });
    return response.data;
  },
  getUserParticipations: async (userId: number) => {
    const response = await api.get<ParticipationDTO[]>(`/participations/user/${userId}`);
    return response.data;
  },
};
