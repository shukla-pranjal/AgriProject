import React from 'react';
import './Loading.css';

const Loading = ({ fullPage = false, size = 'md', text = 'Loading...' }) => {
    if (fullPage) {
        return (
            <div className="loading-fullpage">
                <div className="loading-content">
                    <div className={`loading-spinner loading-spinner-${size}`}></div>
                    {text && <p className="loading-text">{text}</p>}
                </div>
            </div>
        );
    }

    return (
        <div className="loading-inline">
            <div className={`loading-spinner loading-spinner-${size}`}></div>
            {text && <span className="loading-text">{text}</span>}
        </div>
    );
};

export default Loading;
