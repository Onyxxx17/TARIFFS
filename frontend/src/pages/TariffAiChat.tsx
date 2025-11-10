import { useState } from "react";
import { fetchWithAuth } from "../utils/api";


interface Message {
  id: string;
  type: 'user' | 'ai';
  content: string;
  timestamp: Date;
  isError?: boolean;
}

interface ChatResponse {
  message: string;
  timestamp: string;
  isError: boolean;
}


export default function TariffAIChat() {
  const [messages, setMessages] = useState<Message[]>([
    {
      id: '1',
      type: 'ai',
      content: 'Hello! I\'m your AI assistant specialized in international trade tariffs and customs. I can help you understand:\n\n• What tariffs are and how they work\n• How to calculate tariff costs\n• Different types of trade fees\n• Import/export regulations\n• Country-specific trade information\n\nFeel free to ask me any questions about tariffs! You can also use the tariff calculator tool on this platform for specific calculations. How can I help you today?',
      timestamp: new Date(),
      isError: false
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
      const response = await fetchWithAuth('/api/chat/message', {
        method: 'POST',
        body: JSON.stringify({
          message: inputValue
        })
      });


      if (!response.ok) {
        if (response.status === 401) {
          throw new Error('UNAUTHORIZED');
        }
        throw new Error('Failed to get AI response');
      }


      const data: ChatResponse = await response.json();


      const aiMessage: Message = {
        id: (Date.now() + 1).toString(),
        type: 'ai',
        content: data.message,
        timestamp: new Date(data.timestamp),
        isError: data.isError
      };


      setMessages(prev => [...prev, aiMessage]);
    } catch (error) {
      let errorContent = 'I apologize, but I encountered an error processing your request. Please try again or rephrase your question.';
      
      if (error instanceof Error && error.message === 'UNAUTHORIZED') {
        errorContent = 'Your session has expired or you are not authorized to access this feature. Please log in again to continue.';
      }

      const errorMessage: Message = {
        id: (Date.now() + 1).toString(),
        type: 'ai',
        content: errorContent,
        timestamp: new Date(),
        isError: true
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
                 
                  {/* Show error indicator if this is an error message */}
                  {message.isError && (
                    <div className="mt-2 text-xs text-red-600 dark:text-red-400">
                      ⚠️ Error occurred
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