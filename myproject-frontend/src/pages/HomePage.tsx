import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { pollService } from '../services/pollService';
import { participationService } from '../services/participationService';
import { authService } from '../services/authService';
import type { PollDTO } from '../types';

const HomePage = () => {
  const [allPolls, setAllPolls] = useState<PollDTO[]>([]);
  const [pendingPolls, setPendingPolls] = useState<PollDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const currentUser = authService.getCurrentUser();

  useEffect(() => {
    if (!currentUser) {
      setLoading(false);
      return;
    }
    const loadData = async () => {
      try {
        const polls = await pollService.getAllPolls(currentUser.id);
        setAllPolls(polls);
        const pending = await participationService.getPendingPolls(currentUser.id);
        setPendingPolls(pending);
      } catch (error) {
        console.error('Error loading polls', error);
      } finally {
        setLoading(false);
      }
    };
    loadData();
  }, []);

  if (loading) {
    return (
      <div className="d-flex flex-column align-items-center justify-content-center" style={{ minHeight: '50vh' }}>
        <div className="spinner-border text-primary mb-4" role="status" style={{ width: '3rem', height: '3rem' }}>
          <span className="visually-hidden">Loading...</span>
        </div>
        <h4 className="text-secondary fw-semibold">Loading Polls...</h4>
      </div>
    );
  }

  if (!currentUser) {
    return (
      <div className="fade-in-up py-5 text-center">
        <div className="mx-auto p-5 rounded-4 glass-panel shadow-lg" style={{ maxWidth: '800px', marginTop: '2rem' }}>
          <div className="mb-4 d-inline-flex align-items-center justify-content-center" style={{ width: '80px', height: '80px', borderRadius: '50%', backgroundColor: 'var(--accent-light)', border: '1px solid var(--accent-border)' }}>
            <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="var(--accent)" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" style={{ filter: 'drop-shadow(0 0 8px var(--accent-border))' }}>
              <path d="M12 20V10" />
              <path d="M18 20V4" />
              <path d="M6 20v-4" />
            </svg>
          </div>
          <h1 className="display-5 fw-bold mb-3" style={{ letterSpacing: '-0.03em' }}>
            Welcome to PollApp
          </h1>
          <p className="lead text-secondary mb-5 mx-auto" style={{ maxWidth: '600px', fontSize: '1.15rem', lineHeight: '1.7' }}>
            Create custom polls, invite your team, cast your votes, and track live progress with interactive, real-time results. Log in or register to get started.
          </p>
          <div className="d-flex justify-content-center gap-3 flex-wrap">
            <Link to="/login" className="btn btn-primary btn-lg px-5 shadow-sm">
              Login to View Polls
            </Link>
            <Link to="/register" className="btn btn-secondary btn-lg px-5">
              Register Now
            </Link>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="fade-in-up text-start">
      {currentUser && (
        <section className="mb-5">
          <div className="section-header">
            <h2 className="mb-0">Pending Invitations</h2>
            <Link to="/create-poll" className="btn btn-primary">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                <line x1="12" y1="5" x2="12" y2="19" />
                <line x1="5" y1="12" x2="19" y2="12" />
              </svg>
              Create New Poll
            </Link>
          </div>
          {pendingPolls.length > 0 ? (
            <div className="row">
              {pendingPolls.map((poll) => (
                <div key={poll.id} className="col-lg-4 col-md-6 col-sm-12 mb-4">
                  <div className="card h-100 border-warning" style={{ borderStyle: 'dashed', backgroundColor: 'rgba(245, 158, 11, 0.01)' }}>
                    <div className="card-body d-flex flex-column">
                      <h5 className="card-title fw-bold" style={{ color: 'var(--warning)' }}>{poll.title}</h5>
                      <p className="card-text text-truncate mb-4">{poll.description}</p>
                      <div className="text-muted small mb-4 mt-auto">
                        <div className="mb-1">
                          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" style={{ marginRight: '6px', verticalAlign: 'text-bottom' }}>
                            <rect x="3" y="4" width="18" height="18" rx="2" ry="2" />
                            <line x1="16" y1="2" x2="16" y2="6" />
                            <line x1="8" y1="2" x2="8" y2="6" />
                            <line x1="3" y1="10" x2="21" y2="10" />
                          </svg>
                          <strong>Due:</strong> {new Date(poll.dueDate).toLocaleString()}
                        </div>
                        <div>
                          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" style={{ marginRight: '6px', verticalAlign: 'text-bottom' }}>
                            <circle cx="12" cy="12" r="10" />
                            <path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3" />
                            <line x1="12" y1="17" x2="12.01" y2="17" />
                          </svg>
                          <strong>Questions:</strong> {poll.questions.length}
                        </div>
                      </div>
                      <Link to={`/poll/${poll.id}`} className="btn btn-primary w-100">Participate</Link>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p className="text-muted">No pending invitations found.</p>
          )}
        </section>
      )}

      <section>
        <h2 className="mb-4">All Polls</h2>
        <div className="row">
          {allPolls.map((poll) => (
            <div key={poll.id} className="col-lg-4 col-md-6 col-sm-12 mb-4">
              <div className="card h-100">
                <div className="card-body d-flex flex-column">
                  <h5 className="card-title">{poll.title}</h5>
                  <p className="card-text text-truncate flex-grow-1 mb-3">{poll.description}</p>
                  <p className="text-muted small mb-4">
                    <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" style={{ marginRight: '4px', verticalAlign: 'text-bottom' }}>
                      <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
                      <circle cx="12" cy="7" r="4" />
                    </svg>
                    By: {poll.creatorUsername}
                  </p>
                  <div className="d-flex justify-content-between align-items-center mt-auto pt-2 border-top border-light-subtle" style={{ borderColor: 'var(--border-color) !important' }}>
                    <Link to={`/poll/${poll.id}`} className="btn btn-secondary btn-sm">
                      {poll.finished ? 'View Results' : 'View Details'}
                    </Link>
                    {poll.finished ? (
                      <span className="badge bg-success">Finished</span>
                    ) : (
                      <span className="badge bg-info text-dark">Active</span>
                    )}
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
};

export default HomePage;
