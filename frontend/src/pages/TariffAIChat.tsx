import { useState } from "react";
import { fetchWithAuth } from "../utils/api";

interface Message {
  id: string;
  type: 'user' | 'ai';
  content: string;
  timestamp: Date;
  data?: any[];
  sqlQuery?: string;
}

interface TariffQueryResponse {
  response: string;
  queryType: string;
  data?: any[];
  sqlQuery?: string;
  hasData: boolean;
}

export default function TariffAIChat() {
  const [messages, setMessages] = useState<Message[]>([
    {
      id: '1',
      type: 'ai',
      content: 'Hello! I\'m your AI assistant for tariff and trade data. You can ask me questions like:\n\n• "What are the tariff rates for China?"\n• "Show me products in the agriculture category"\n• "List countries with tariff data"\n• "What are the current tariff rates between US and Germany?"\n\nI can extract real data from our database and provide analysis. How can I help you today?',
      timestamp: new Date()
    }
  ]);
  const [inputValue, setInputValue] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleSendMessage = async () => {
    if (!inputValue.trim() || isLoading) return;

    const userMessage: Message = {
      id: Date.now().toString(),
      type: 'user',
      content: inputValue,
      timestamp: new Date()
    };

    setMessages(prev => [...prev, userMessage]);
    setInputValue('');
    setIsLoading(true);

    try {
      const response = await fetchWithAuth('/api/ai/query', {
        method: 'POST',
        body: JSON.stringify({
          query: inputValue,
          queryType: 'general'
        })
      });

      if (!response.ok) {
        throw new Error('Failed to get AI response');
      }

      const data: TariffQueryResponse = await response.json();

      const aiMessage: Message = {
        id: (Date.now() + 1).toString(),
        type: 'ai',
        content: data.response,
        timestamp: new Date(),
        data: data.data,
        sqlQuery: data.sqlQuery
      };

      setMessages(prev => [...prev, aiMessage]);
    } catch (error) {
      const errorMessage: Message = {
        id: (Date.now() + 1).toString(),
        type: 'ai',
        content: 'I apologize, but I encountered an error processing your request. Please try again or rephrase your question.',
        timestamp: new Date()
      };
      setMessages(prev => [...prev, errorMessage]);
    } finally {
      setIsLoading(false);
    }
  };

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSendMessage();
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 py-8 px-4 sm:px-6 lg:px-8">
      <div className="max-w-4xl mx-auto">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-extrabold text-gray-900">Tariff AI Assistant</h1>
          <p className="mt-4 text-lg text-gray-500">
            Ask questions about tariffs, countries, products, and trade data
          </p>
        </div>

        <div className="bg-white rounded-lg shadow-sm border border-gray-200 flex flex-col h-[600px]">
          {/* Messages Area */}
          <div className="flex-1 overflow-y-auto p-6 space-y-4">
            {messages.map((message) => (
              <div
                key={message.id}
                className={`flex ${message.type === 'user' ? 'justify-end' : 'justify-start'}`}
              >
                <div
                  className={`max-w-3xl rounded-lg px-4 py-3 ${
                    message.type === 'user'
                      ? 'bg-blue-600 text-white'
                      : 'bg-gray-100 text-gray-900'
                  }`}
                >
                  <div className="whitespace-pre-wrap">{message.content}</div>
                  
                  {/* Show SQL Query if available */}
                  {message.sqlQuery && (
                    <div className="mt-3 p-3 bg-gray-800 text-green-400 rounded text-sm font-mono">
                      <div className="text-xs text-gray-400 mb-1">SQL Query:</div>
                      {message.sqlQuery}
                    </div>
                  )}
                  
                  {/* Show Data Table if available */}
                  {message.data && message.data.length > 0 && (
                    <div className="mt-3 overflow-x-auto">
                      <table className="min-w-full text-sm border border-gray-300">
                        <thead className="bg-gray-50">
                          <tr>
                            {Object.keys(message.data[0]).map((key) => (
                              <th key={key} className="px-3 py-2 text-left font-medium text-gray-700 border-b">
                                {key}
                              </th>
                            ))}
                          </tr>
                        </thead>
                        <tbody>
                          {message.data.slice(0, 10).map((row, idx) => (
                            <tr key={idx} className="border-b">
                              {Object.values(row).map((value, cellIdx) => (
                                <td key={cellIdx} className="px-3 py-2 text-gray-900">
                                  {value?.toString() || 'N/A'}
                                </td>
                              ))}
                            </tr>
                          ))}
                        </tbody>
                      </table>
                      {message.data.length > 10 && (
                        <div className="text-xs text-gray-500 mt-2">
                          Showing first 10 of {message.data.length} results
                        </div>
                      )}
                    </div>
                  )}
                  
                  <div className="text-xs opacity-70 mt-2">
                    {message.timestamp.toLocaleTimeString()}
                  </div>
                </div>
              </div>
            ))}
            
            {isLoading && (
              <div className="flex justify-start">
                <div className="bg-gray-100 text-gray-900 rounded-lg px-4 py-3">
                  <div className="flex items-center space-x-2">
                    <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-blue-600"></div>
                    <span>AI is thinking...</span>
                  </div>
                </div>
              </div>
            )}
          </div>

          {/* Input Area */}
          <div className="border-t border-gray-200 p-4">
            <div className="flex space-x-3">
              <textarea
                value={inputValue}
                onChange={(e) => setInputValue(e.target.value)}
                onKeyPress={handleKeyPress}
                placeholder="Ask about tariffs, countries, products, or trade data..."
                className="flex-1 resize-none border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                rows={2}
                disabled={isLoading}
              />
              <button
                onClick={handleSendMessage}
                disabled={!inputValue.trim() || isLoading}
                className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Send
              </button>
            </div>
            <div className="mt-2 text-xs text-gray-500">
              Press Enter to send, Shift+Enter for new line
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}