import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { addBus } from '../services/api';

const AddBus = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    name: '',
    route: '',
    departureDate: '',
    departureTime: '',
    arrivalTime: '',
    availableSeats: '',
    totalSeats: '',
    price: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const formatDate = (dateString) => {
    if (!dateString) return '';
    try {
      console.log('Formatting date for backend:', dateString); // Debug log
      // If the date is already in dd-mm-yyyy format, return it as is
      if (dateString.match(/^\d{2}-\d{2}-\d{4}$/)) {
        return dateString;
      }
      // If the date is in yyyy-mm-dd format, convert it to dd-mm-yyyy
      if (dateString.match(/^\d{4}-\d{2}-\d{2}$/)) {
        const [year, month, day] = dateString.split('-');
        return `${day}-${month}-${year}`;
      }
      // Otherwise, try to parse and format it
      const date = new Date(dateString);
      if (isNaN(date.getTime())) {
        return 'Invalid date';
      }
      return date.toLocaleDateString('en-GB', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
      }).replace(/\//g, '-');
    } catch (error) {
      console.error('Error formatting date:', error);
      return 'Invalid date';
    }
  };

  const formatDateForInput = (dateString) => {
    if (!dateString) return '';
    try {
      // If the date is already in dd-mm-yyyy format, convert it to yyyy-mm-dd
      if (dateString.match(/^\d{2}-\d{2}-\d{4}$/)) {
        const [day, month, year] = dateString.split('-');
        return `${year}-${month}-${day}`;
      }
      // Otherwise, try to parse and format it
      const date = new Date(dateString);
      if (isNaN(date.getTime())) {
        return '';
      }
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    } catch (error) {
      console.error('Error formatting date for input:', error);
      return '';
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === 'departureDate') {
      // Store the date in dd-mm-yyyy format for backend
      setFormData(prev => ({
        ...prev,
        [name]: formatDate(value)
      }));
    } else {
      setFormData(prev => ({
        ...prev,
        [name]: value
      }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      // Convert string values to numbers where needed
      const busData = {
        ...formData,
        availableSeats: parseInt(formData.availableSeats),
        totalSeats: parseInt(formData.totalSeats),
        price: parseFloat(formData.price),
        departureDate: formatDate(formData.departureDate) // Ensure date is in dd-mm-yyyy format
      };
      
      console.log('Sending bus data:', busData); // Debug log
      await addBus(busData);
      navigate('/buses');
    } catch (err) {
      console.error('Error adding bus:', err);
      setError('Failed to add bus');
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <div className="page-header">
        <h1 className="page-title">Add New Bus</h1>
      </div>

      {error && (
        <div className="error-message">
          <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          {error}
        </div>
      )}

      <div className="card">
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="form-label" htmlFor="name">Bus Name</label>
            <input
              type="text"
              id="name"
              name="name"
              className="form-input"
              value={formData.name}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label className="form-label" htmlFor="route">Route</label>
            <input
              type="text"
              id="route"
              name="route"
              className="form-input"
              value={formData.route}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label className="form-label" htmlFor="departureDate">Departure Date</label>
            <input
              type="date"
              id="departureDate"
              name="departureDate"
              className="form-input"
              value={formData.departureDate ? formatDateForInput(formData.departureDate) : ''}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label className="form-label" htmlFor="departureTime">Departure Time</label>
            <input
              type="time"
              id="departureTime"
              name="departureTime"
              className="form-input"
              value={formData.departureTime}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label className="form-label" htmlFor="arrivalTime">Arrival Time</label>
            <input
              type="time"
              id="arrivalTime"
              name="arrivalTime"
              className="form-input"
              value={formData.arrivalTime}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label className="form-label" htmlFor="totalSeats">Total Seats</label>
            <input
              type="number"
              id="totalSeats"
              name="totalSeats"
              className="form-input"
              value={formData.totalSeats}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label className="form-label" htmlFor="availableSeats">Available Seats</label>
            <input
              type="number"
              id="availableSeats"
              name="availableSeats"
              className="form-input"
              value={formData.availableSeats}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label className="form-label" htmlFor="price">Price</label>
            <input
              type="number"
              id="price"
              name="price"
              className="form-input"
              value={formData.price}
              onChange={handleChange}
              required
            />
          </div>

          <div className="flex gap-4">
            <button
              type="button"
              className="btn btn-secondary"
              onClick={() => navigate('/buses')}
            >
              Cancel
            </button>
            <button
              type="submit"
              className="btn btn-primary"
              disabled={loading}
            >
              {loading ? (
                <>
                  <div className="loading-spinner"></div>
                  Adding...
                </>
              ) : (
                'Add Bus'
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddBus; 