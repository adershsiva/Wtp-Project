import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { pollService } from '../services/pollService';
import { participationService } from '../services/participationService';
import { resultService } from '../services/resultService';
import { authService } from '../services/authService';
import { QuestionType } from '../types';
import type { PollDTO, ParticipationDTO, PollResultDTO } from '../types';

const PollDetailPage = () => {
  const { id } = useParams<{ id: string }>();
  const [poll, setPoll] = useState<PollDTO | null>(null);
  const [results, setResults] = useState<PollResultDTO | null>(null);
  const [individualResponses, setIndividualResponses] = useState<ParticipationDTO[]>([]);
  const [answers, setAnswers] = useState<{ [key: number]: string }>({});
  const [inviteUsername, setInviteUsername] = useState('');
  const [loading, setLoading] = useState(true);
  const [isEditing, setIsEditing] = useState(false);
  const [editTitle, setEditTitle] = useState('');
  const [editDescription, setEditDescription] = useState('');
  const [editDueDate, setEditDueDate] = useState('');
  const navigate = useNavigate();
  const currentUser = authService.getCurrentUser();

  const isCreator = poll?.creatorUsername === currentUser?.username;
  const isExpired = poll ? new Date(poll.dueDate) < new Date() : false;
  const isFinished = poll?.finished;

  useEffect(() => {
    if (id) {
      loadPollData(parseInt(id));
    }
  }, [id]);

  const loadPollData = async (pollId: number) => {
    try {
      const data = await pollService.getPoll(pollId);
      setPoll(data);
      if (data.finished && currentUser && data.creatorUsername === currentUser.username) {
        const res = await resultService.getAggregateResults(pollId, currentUser.id);
        setResults(res);
        const ind = await resultService.getIndividualResponses(pollId, currentUser.id);
        setIndividualResponses(ind);
      }
    } catch (error) {
      console.error('Error loading poll', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAnswerChange = (questionId: number, value: string) => {
    setAnswers({ ...answers, [questionId]: value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!currentUser) {
      alert('Please login first');
      return;
    }

    if (Object.keys(answers).length !== poll?.questions.length) {
      alert('Please answer all questions before submitting.');
      return;
    }

    const participation: ParticipationDTO = {
      pollId: poll!.id!,
      answers: Object.entries(answers).map(([qId, content]) => ({
        questionId: parseInt(qId),
        content
      }))
    };

    try {
      await participationService.participate(participation, currentUser.id);
      alert('Thank you for participating!');
      navigate('/');
    } catch (error: any) {
      console.error('Error participating', error);
      alert(error.response?.data?.message || 'Failed to submit answers.');
    }
  };

  const startEditing = () => {
    if (poll) {
      setEditTitle(poll.title);
      setEditDescription(poll.description);
      const date = new Date(poll.dueDate);
      const tzoffset = date.getTimezoneOffset() * 60000;
      const localISOTime = (new Date(date.getTime() - tzoffset)).toISOString().slice(0, 16);
      setEditDueDate(localISOTime);
      setIsEditing(true);
    }
  };

  const handleUpdatePoll = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!poll || !currentUser) return;
    try {
      const updatedPollDto: PollDTO = {
        ...poll,
        title: editTitle,
        description: editDescription,
        dueDate: new Date(editDueDate).toISOString(),
      };
      await pollService.updatePoll(poll.id!, updatedPollDto, currentUser.id);
      alert('Poll updated successfully!');
      setIsEditing(false);
      loadPollData(poll.id!);
    } catch (error: any) {
      alert(error.response?.data?.message || 'Failed to update poll.');
    }
  };

  const handleInvite = async () => {
    if (!inviteUsername) return;
    try {
      await pollService.inviteUser(poll!.id!, inviteUsername, currentUser!.id);
      alert(`User ${inviteUsername} invited!`);
      setInviteUsername('');
      loadPollData(poll!.id!);
    } catch (error: any) {
      alert(error.response?.data?.message || 'Failed to invite user.');
    }
  };

  const handleFinish = async () => {
    if (!window.confirm('Are you sure you want to finish this poll? No more responses will be allowed.')) return;
    try {
      await pollService.finishPoll(poll!.id!, currentUser!.id);
      alert('Poll finished!');
      loadPollData(poll!.id!);
    } catch (error: any) {
      alert(error.response?.data?.message || 'Failed to finish poll.');
    }
  };

  const handleDelete = async () => {
    if (!window.confirm('Are you sure you want to completely delete this poll? This action cannot be undone.')) return;
    try {
      await pollService.deletePoll(poll!.id!, currentUser!.id);
      alert('Poll deleted successfully.');
      navigate('/');
    } catch (error: any) {
      alert(error.response?.data?.message || 'Failed to delete poll.');
    }
  };

  if (loading) {
    return (
      <div className="d-flex flex-column align-items-center justify-content-center" style={{ minHeight: '50vh' }}>
        <div className="spinner-border text-primary mb-4" role="status" style={{ width: '3rem', height: '3rem' }}>
          <span className="visually-hidden">Loading...</span>
        </div>
        <h4 className="text-secondary fw-semibold">Loading Poll Details...</h4>
      </div>
    );
  }
  if (!poll) {
    return (
      <div className="text-center py-5">
        <h3 className="text-danger">Poll Not Found</h3>
        <p className="text-muted">The poll you are looking for might have been deleted or doesn't exist.</p>
        <button className="btn btn-secondary mt-3" onClick={() => navigate('/')}>Return Home</button>
      </div>
    );
  }

  return (
    <div className="fade-in-up text-start mb-5" style={{ maxWidth: '900px', margin: '0 auto' }}>
      {isEditing ? (
        <form onSubmit={handleUpdatePoll} className="card p-4 mb-4 border-primary" style={{ backgroundColor: 'rgba(var(--accent-rgb), 0.02)' }}>
          <h3 className="mb-4 fw-bold">Edit Poll Settings</h3>
          <div className="mb-3">
            <label className="form-label fw-semibold">Poll Title</label>
            <input 
              type="text" 
              className="form-control" 
              value={editTitle} 
              onChange={(e) => setEditTitle(e.target.value)} 
              required 
            />
          </div>
          <div className="mb-3">
            <label className="form-label fw-semibold">Description</label>
            <textarea 
              className="form-control" 
              rows={3} 
              value={editDescription} 
              onChange={(e) => setEditDescription(e.target.value)} 
              required 
            />
          </div>
          <div className="mb-3">
            <label className="form-label fw-semibold">Due Date</label>
            <input 
              type="datetime-local" 
              className="form-control" 
              value={editDueDate} 
              onChange={(e) => setEditDueDate(e.target.value)} 
              required 
            />
          </div>
          <div className="d-flex gap-2 mt-4">
            <button type="submit" className="btn btn-primary">Save Changes</button>
            <button type="button" className="btn btn-secondary" onClick={() => setIsEditing(false)}>Cancel</button>
          </div>
        </form>
      ) : (
        <div className="d-flex justify-content-between align-items-start gap-4 mb-4 flex-wrap">
          <div>
            <h1 className="mb-2">{poll.title}</h1>
            <p className="lead text-secondary mb-3">{poll.description}</p>
            <div className="text-muted small d-flex flex-wrap gap-3">
              <span>
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" style={{ marginRight: '6px', verticalAlign: 'text-bottom' }}>
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
                  <circle cx="12" cy="7" r="4" />
                </svg>
                Creator: <strong className="text-dark-emphasis">{poll.creatorUsername}</strong>
              </span>
              <span>
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" style={{ marginRight: '6px', verticalAlign: 'text-bottom' }}>
                  <rect x="3" y="4" width="18" height="18" rx="2" ry="2" />
                  <line x1="16" y1="2" x2="16" y2="6" />
                  <line x1="8" y1="2" x2="8" y2="6" />
                  <line x1="3" y1="10" x2="21" y2="10" />
                </svg>
                Due Date: <strong className="text-dark-emphasis">{new Date(poll.dueDate).toLocaleString()}</strong>
              </span>
            </div>
          </div>
          <div className="d-flex gap-2">
            {isCreator && !isFinished && (
              <button className="btn btn-outline-primary" onClick={startEditing}>
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" style={{ marginRight: '4px' }}>
                  <path d="M12 20h9" />
                  <path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z" />
                </svg>
                Edit Poll
              </button>
            )}
            {isCreator && !isFinished && (
              <button className="btn btn-warning" onClick={handleFinish}>
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" style={{ marginRight: '4px' }}>
                  <circle cx="12" cy="12" r="10" />
                  <polyline points="12 6 12 12 16 14" />
                </svg>
                Finish Poll
              </button>
            )}
            {isCreator && (
              <button className="btn btn-danger" onClick={handleDelete}>
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" style={{ marginRight: '4px' }}>
                  <polyline points="3 6 5 6 21 6" />
                  <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2" />
                </svg>
                Delete
              </button>
            )}
          </div>
        </div>
      )}
      <hr className="my-4" style={{ borderColor: 'var(--border-color)' }} />

      {isCreator && (
        <section className="mb-5 card p-4 border-warning" style={{ borderStyle: 'dashed', backgroundColor: 'rgba(245, 158, 11, 0.02)' }}>
          <h4 className="mb-4 d-flex align-items-center gap-2">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" className="text-warning">
              <rect x="3" y="11" width="18" height="11" rx="2" ry="2" />
              <path d="M7 11V7a5 5 0 0 1 10 0v4" />
            </svg>
            Admin Control Panel
          </h4>
          {!isFinished && (
            <div className="mb-4" style={{ maxWidth: '500px' }}>
              <label className="form-label">Invite User by Username</label>
              <div className="input-group">
                <input 
                  type="text" 
                  className="form-control" 
                  value={inviteUsername} 
                  onChange={(e) => setInviteUsername(e.target.value)} 
                  placeholder="Enter username"
                />
                <button className="btn btn-primary" onClick={handleInvite}>Invite User</button>
              </div>
              <div className="mt-3 small text-muted">
                <strong>Invited Users:</strong> {poll.invitedUsernames && poll.invitedUsernames.length > 0 ? (
                  poll.invitedUsernames.map(name => (
                    <span key={name} className="badge bg-secondary me-1" style={{ fontSize: '0.8rem' }}>{name}</span>
                  ))
                ) : 'None yet'}
              </div>
            </div>
          )}

          {isFinished ? (
            <div className="fade-in-up">
              <h5 className="mt-4 mb-3 fw-bold">Aggregate Results</h5>
              {results?.questionResults.map((qr) => (
                <div key={qr.questionId} className="card mb-3" style={{ transition: 'none', transform: 'none' }}>
                  <div className="card-body p-4">
                    <h6 className="card-title fw-bold mb-3">{qr.content}</h6>
                    {qr.average !== undefined && qr.average !== null && (
                      <div className="mb-3 d-flex align-items-center gap-2">
                        <span className="text-muted small">Average Rating:</span>
                        <span className="fs-5 fw-bold text-primary">{qr.average.toFixed(2)}</span>
                        <span className="text-muted small">/ 5.00</span>
                      </div>
                    )}
                    {qr.distribution && (
                      <div className="mb-3">
                        <div className="text-muted small mb-2">Vote Distribution:</div>
                        <div className="d-flex flex-wrap gap-2">
                          {Object.entries(qr.distribution).map(([val, count]) => (
                            <span key={val} className="badge bg-info text-dark" style={{ fontSize: '0.8rem' }}>
                              Option "{val}": <strong>{count}</strong> {count === 1 ? 'vote' : 'votes'}
                            </span>
                          ))}
                        </div>
                      </div>
                    )}
                    {qr.textResponses && qr.textResponses.length > 0 && (
                      <div>
                        <div className="text-muted small mb-2">Text Responses:</div>
                        <ul className="list-group list-group-flush border rounded overflow-hidden">
                          {qr.textResponses.map((res, i) => (
                            <li key={i} className="list-group-item bg-transparent text-secondary small py-2 px-3">{res}</li>
                          ))}
                        </ul>
                      </div>
                    )}
                  </div>
                </div>
              ))}

              <h5 className="mt-5 mb-3 fw-bold">Individual Responses</h5>
              <div className="table-responsive">
                <table className="table">
                  <thead>
                    <tr>
                      <th>Participant</th>
                      {poll.questions.map(q => <th key={q.id}>{q.content}</th>)}
                    </tr>
                  </thead>
                  <tbody>
                    {individualResponses.map((ir, i) => (
                      <tr key={i}>
                        <td className="fw-bold text-primary">{ir.username}</td>
                        {poll.questions.map(q => {
                          const answer = ir.answers?.find(a => a.questionId === q.id);
                          return <td key={q.id}>{answer?.content || <span className="text-muted italic">-</span>}</td>;
                        })}
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          ) : (
            <div className="alert alert-info d-flex align-items-center gap-3">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                <circle cx="12" cy="12" r="10" />
                <line x1="12" y1="16" x2="12" y2="12" />
                <line x1="12" y1="8" x2="12.01" y2="8" />
              </svg>
              <div>Results and statistics will be unlocked here once you mark this poll as <strong>Finished</strong>.</div>
            </div>
          )}
        </section>
      )}

      {(!isCreator || isFinished) && (
        <section className="fade-in-up">
          <h3 className="mb-4 fw-bold">{isFinished ? 'Poll Finished' : 'Submit Your Responses'}</h3>
          {isFinished ? (
            <div className="alert alert-secondary d-flex align-items-center gap-3">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                <rect x="3" y="11" width="18" height="11" rx="2" ry="2" />
                <path d="M7 11V7a5 5 0 0 1 10 0v4" />
              </svg>
              <div>This poll is closed. No more responses are being accepted.</div>
            </div>
          ) : isExpired ? (
            <div className="alert alert-danger d-flex align-items-center gap-3">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                <circle cx="12" cy="12" r="10" />
                <line x1="12" y1="8" x2="12" y2="12" />
                <line x1="12" y1="16" x2="12.01" y2="16" />
              </svg>
              <div>The due date for this poll has passed. Participation is closed.</div>
            </div>
          ) : (
            <form onSubmit={handleSubmit} className="mt-3">
              {poll.questions.map((q, idx) => (
                <div key={q.id} className="poll-question-card">
                  <h5 className="mb-3 fw-bold" style={{ fontSize: '1.05rem' }}>
                    <span className="text-muted me-2">{idx + 1}.</span> {q.content}
                  </h5>
                  {q.type === QuestionType.PLAIN_TEXT && (
                    <textarea 
                      className="form-control" 
                      rows={3}
                      placeholder="Type your answer here..."
                      onChange={(e) => handleAnswerChange(q.id!, e.target.value)} 
                      required
                    />
                  )}
                  {q.type === QuestionType.BOOLEAN && (
                    <div className="d-flex gap-3 mt-2 flex-wrap">
                      <label 
                        className="form-check-label d-flex align-items-center gap-2 px-3 py-2 border rounded" 
                        style={{ 
                          cursor: 'pointer', 
                          transition: 'all 0.2s', 
                          borderColor: answers[q.id!] === 'Yes' ? 'var(--accent)' : 'var(--border-color)', 
                          backgroundColor: answers[q.id!] === 'Yes' ? 'var(--accent-light)' : 'transparent',
                          borderRadius: 'var(--radius-md)'
                        }}
                      >
                        <input 
                          className="form-check-input" 
                          type="radio" 
                          name={`q-${q.id}`} 
                          value="Yes" 
                          id={`q-${q.id}-yes`}
                          checked={answers[q.id!] === 'Yes'}
                          onChange={(e) => handleAnswerChange(q.id!, e.target.value)} 
                          required
                        />
                        Yes
                      </label>
                      <label 
                        className="form-check-label d-flex align-items-center gap-2 px-3 py-2 border rounded" 
                        style={{ 
                          cursor: 'pointer', 
                          transition: 'all 0.2s', 
                          borderColor: answers[q.id!] === 'No' ? 'var(--accent)' : 'var(--border-color)', 
                          backgroundColor: answers[q.id!] === 'No' ? 'var(--accent-light)' : 'transparent',
                          borderRadius: 'var(--radius-md)'
                        }}
                      >
                        <input 
                          className="form-check-input" 
                          type="radio" 
                          name={`q-${q.id}`} 
                          value="No" 
                          id={`q-${q.id}-no`}
                          checked={answers[q.id!] === 'No'}
                          onChange={(e) => handleAnswerChange(q.id!, e.target.value)} 
                        />
                        No
                      </label>
                    </div>
                  )}
                  {q.type === QuestionType.NUMERIC_1_TO_5 && (
                    <div className="d-flex flex-column gap-2 mt-2">
                      <div className="numeric-scale-container">
                        {[1, 2, 3, 4, 5].map(v => (
                          <div 
                            key={v} 
                            className={`numeric-scale-btn ${answers[q.id!] === String(v) ? 'active' : ''}`}
                            onClick={() => handleAnswerChange(q.id!, String(v))}
                          >
                            {v}
                          </div>
                        ))}
                      </div>
                      <div className="d-flex justify-content-between text-muted small mt-1" style={{ maxWidth: '240px' }}>
                        <span>Low (1)</span>
                        <span>High (5)</span>
                      </div>
                    </div>
                  )}
                </div>
              ))}
              <div className="mt-4">
                <button type="submit" className="btn btn-primary btn-lg w-100 py-3">
                  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                    <line x1="22" y1="2" x2="11" y2="13" />
                    <polygon points="22 2 15 22 11 13 2 9 22 2" />
                  </svg>
                  Submit Responses
                </button>
              </div>
            </form>
          )}
        </section>
      )}
    </div>
  );
};

export default PollDetailPage;
