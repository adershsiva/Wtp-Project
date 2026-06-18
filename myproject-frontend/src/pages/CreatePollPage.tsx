import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { pollService } from '../services/pollService';
import { QuestionType } from '../types';
import type { PollDTO, QuestionDTO } from '../types';
import { authService } from '../services/authService';
import ErrorNotification from '../components/ErrorNotification';

const CreatePollPage = () => {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [dueDate, setDueDate] = useState('');
  const [questions, setQuestions] = useState<QuestionDTO[]>([
    { content: '', type: QuestionType.PLAIN_TEXT }
  ]);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleAddQuestion = () => {
    setQuestions([...questions, { content: '', type: QuestionType.PLAIN_TEXT }]);
  };

  const handleQuestionChange = (index: number, field: keyof QuestionDTO, value: any) => {
    const updatedQuestions = [...questions];
    updatedQuestions[index] = { ...updatedQuestions[index], [field]: value };
    setQuestions(updatedQuestions);
  };

  const handleRemoveQuestion = (index: number) => {
    setQuestions(questions.filter((_, i) => i !== index));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    const currentUser = authService.getCurrentUser();
    if (!currentUser) {
      setError('Please login first');
      return;
    }

    const newPoll: PollDTO = {
      title,
      description,
      dueDate: new Date(dueDate).toISOString(),
      questions
    };

    try {
      await pollService.createPoll(newPoll, currentUser.id);
      alert('Poll created successfully!');
      navigate('/');
    } catch (err: any) {
      console.error('Error creating poll', err);
      setError(err.response?.data?.message || 'Failed to create poll. Title might be taken.');
    }
  };

  return (
    <div className="fade-in-up text-start mb-5" style={{ maxWidth: '800px', margin: '0 auto' }}>
      <h1 className="mb-4">Create New Poll</h1>
      <form onSubmit={handleSubmit} className="card p-4 p-md-5">
        <div className="mb-4">
          <label className="form-label">Title</label>
          <input 
            type="text" 
            className="form-control" 
            placeholder="Give your poll a clear, descriptive title"
            value={title} 
            onChange={(e) => setTitle(e.target.value)} 
            required 
          />
        </div>
        <div className="mb-4">
          <label className="form-label">Description</label>
          <textarea 
            className="form-control" 
            rows={3}
            placeholder="Explain the purpose of this poll to participants..."
            value={description} 
            onChange={(e) => setDescription(e.target.value)} 
            required
          />
        </div>
        <div className="mb-4">
          <label className="form-label">Due Date</label>
          <input 
            type="datetime-local" 
            className="form-control" 
            value={dueDate} 
            onChange={(e) => setDueDate(e.target.value)} 
            required 
          />
        </div>
        
        <h3 className="mt-5 mb-3">Questions</h3>
        {questions.map((q, index) => (
          <div key={index} className="poll-question-card">
            <div className="d-flex justify-content-between align-items-center mb-3">
              <label className="form-label mb-0" style={{ fontSize: '1.05rem', color: 'var(--accent)' }}>Question {index + 1}</label>
              <button 
                type="button" 
                className="btn btn-sm btn-danger" 
                onClick={() => handleRemoveQuestion(index)}
                disabled={questions.length === 1}
                style={{ padding: '0.35rem 0.75rem' }}
              >
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                  <polyline points="3 6 5 6 21 6" />
                  <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2" />
                </svg>
                Remove
              </button>
            </div>
            <input 
              type="text" 
              className="form-control mb-3" 
              placeholder="e.g., What features would you like to see next?"
              value={q.content} 
              onChange={(e) => handleQuestionChange(index, 'content', e.target.value)} 
              required 
            />
            <div className="row">
              <div className="col-md-6">
                <label className="form-label small text-muted">Answer Type</label>
                <select 
                  className="form-select" 
                  value={q.type} 
                  onChange={(e) => handleQuestionChange(index, 'type', e.target.value as QuestionType)}
                >
                  <option value={QuestionType.PLAIN_TEXT}>Plain Text Response</option>
                  <option value={QuestionType.BOOLEAN}>Yes / No</option>
                  <option value={QuestionType.NUMERIC_1_TO_5}>Rating Scale (1 to 5)</option>
                </select>
              </div>
            </div>
          </div>
        ))}
        
        <div className="mb-4">
          <button type="button" className="btn btn-secondary" onClick={handleAddQuestion}>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
              <line x1="12" y1="5" x2="12" y2="19" />
              <line x1="5" y1="12" x2="19" y2="12" />
            </svg>
            Add Question
          </button>
        </div>
        
        <ErrorNotification message={error} />

        <div className="border-top pt-4 mt-2 text-end">
          <button type="submit" className="btn btn-primary btn-lg">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
              <path d="M5 12l5 5L20 7" />
            </svg>
            Create Poll
          </button>
        </div>
      </form>
    </div>
  );
};

export default CreatePollPage;
