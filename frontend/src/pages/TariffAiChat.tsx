import { useState, useRef, useEffect } from "react";
import { fetchWithAuth } from "../utils/api";
import { BotMessageSquare, X } from "lucide-react"; // 👈 ADD THIS

interface Message {
  id: string;
  type: "user" | "ai";
  content: string;
  timestamp: Date;
  isError?: boolean;
}

interface ChatResponse {
  message: string;
  timestamp: string;
  isError: boolean;
}

export default function FloatingChatbot() {
  const [isOpen, setIsOpen] = useState(false);
  const [messages, setMessages] = useState<Message[]>([
    {
      id: "1",
      type: "ai",
      content:
        "Hello! I'm your AI assistant specialized in international trade tariffs. I can help you with:\n\n• Product searches\n• Tariff rate calculations\n• Country information\n• Trade regulations\n\nHow can I help you today?",
      timestamp: new Date(),
      isError: false,
    },
  ]);
  const [inputValue, setInputValue] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const handleSendMessage = async () => {
    if (!inputValue.trim() || isLoading) return;

    const userMessage: Message = {
      id: Date.now().toString(),
      type: "user",
      content: inputValue,
      timestamp: new Date(),
    };

    setMessages((prev) => [...prev, userMessage]);
    setInputValue("");
    setIsLoading(true);

    try {
      const response = await fetchWithAuth("/api/chat/message", {
        method: "POST",
        body: JSON.stringify({ message: inputValue }),
      });

      if (!response.ok) {
        if (response.status === 401) throw new Error("UNAUTHORIZED");
        throw new Error("Failed to get AI response");
      }

      const data: ChatResponse = await response.json();

      const aiMessage: Message = {
        id: (Date.now() + 1).toString(),
        type: "ai",
        content: data.message,
        timestamp: new Date(data.timestamp),
        isError: data.isError,
      };

      setMessages((prev) => [...prev, aiMessage]);
    } catch (error) {
      let errorContent =
        "I apologize, but I encountered an error. Please try again.";
      if (error instanceof Error && error.message === "UNAUTHORIZED") {
        errorContent = "Please log in to use the chatbot.";
      }

      const errorMessage: Message = {
        id: (Date.now() + 1).toString(),
        type: "ai",
        content: errorContent,
        timestamp: new Date(),
        isError: true,
      };
      setMessages((prev) => [...prev, errorMessage]);
    } finally {
      setIsLoading(false);
    }
  };

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      handleSendMessage();
    }
  };

  const toggleChat = () => {
    setIsOpen(!isOpen);
  };

  return (
    <>
      {/* Chat Window */}
      {isOpen && (
        <div className="fixed bottom-24 right-6 w-96 h-[520px] bg-white rounded-2xl shadow-2xl border border-gray-200 flex flex-col z-50 animate-slideUp">
          {/* Header */}
          <div className="bg-gradient-to-r from-[#fdfbfb] to-[#ebedee] text-gray-800 px-4 py-3 rounded-t-2xl flex items-center justify-between border-b border-gray-200">
            <div className="flex items-center space-x-2">
              <BotMessageSquare className="w-5 h-5 text-gray-700" />
              <h3 className="font-semibold">Tariff AI Assistant</h3>
            </div>
            <button
              onClick={toggleChat}
              className="text-gray-500 hover:text-gray-700 transition-colors"
            >
              <X className="w-5 h-5" />
            </button>
          </div>

          {/* Messages */}
          <div className="flex-1 overflow-y-auto p-4 space-y-3 bg-[#fafafa]">
            {messages.map((message) => (
              <div
                key={message.id}
                className={`flex ${
                  message.type === "user" ? "justify-end" : "justify-start"
                }`}
              >
                <div
                  className={`max-w-[80%] rounded-2xl px-4 py-2 text-sm leading-relaxed ${
                    message.type === "user"
                      ? "bg-gradient-to-r from-indigo-500 to-purple-500 text-white rounded-br-none"
                      : "bg-white border border-gray-200 text-gray-900 shadow-sm rounded-bl-none"
                  }`}
                >
                  <div className="whitespace-pre-wrap break-words">
                    {message.content}
                  </div>

                  {message.isError && (
                    <div className="mt-1 text-xs text-red-500">⚠️ Error</div>
                  )}

                  <div
                    className={`text-xs mt-1 ${
                      message.type === "user"
                        ? "text-indigo-100"
                        : "text-gray-400"
                    }`}
                  >
                    {message.timestamp.toLocaleTimeString([], {
                      hour: "2-digit",
                      minute: "2-digit",
                    })}
                  </div>
                </div>
              </div>
            ))}

            {isLoading && (
              <div className="flex justify-start">
                <div className="bg-white border border-gray-200 rounded-2xl px-3 py-2 text-sm text-gray-600 shadow-sm">
                  <div className="flex items-center space-x-2">
                    <div className="animate-spin rounded-full h-3 w-3 border-b-2 border-indigo-500"></div>
                    <span>Thinking...</span>
                  </div>
                </div>
              </div>
            )}
            <div ref={messagesEndRef} />
          </div>

          {/* Input */}
          <div className="border-t border-gray-200 p-3 bg-white rounded-b-2xl">
            <div className="flex space-x-2">
              <input
                type="text"
                value={inputValue}
                onChange={(e) => setInputValue(e.target.value)}
                onKeyPress={handleKeyPress}
                placeholder="Ask about tariffs..."
                className="flex-1 border border-gray-300 rounded-full px-4 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-400 focus:border-transparent"
                disabled={isLoading}
              />
              <button
                onClick={handleSendMessage}
                disabled={!inputValue.trim() || isLoading}
                className="px-5 py-2 bg-gradient-to-r from-indigo-500 to-purple-500 text-white rounded-full hover:opacity-90 disabled:opacity-50 disabled:cursor-not-allowed transition text-sm font-medium shadow-sm"
              >
                Send
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Floating Button */}
      <button
        onClick={toggleChat}
        className={`fixed bottom-6 right-6 w-16 h-16 rounded-full shadow-lg flex items-center justify-center transition-all duration-300 z-50 border ${
          isOpen
            ? "bg-slate-200 hover:bg-slate-300 border-gray-300 shadow-inner"
            : "backdrop-blur-md bg-white/60 border-gray-300 shadow-lg hover:bg-white/80"
        }`}
        aria-label="Toggle chat"
      >
        {isOpen ? (
          <X className="w-7 h-7 text-gray-800" />
        ) : (
          <BotMessageSquare className="w-8 h-8 text-gray-800" />
        )}
      </button>

      <style>{`
        @keyframes slideUp {
          from {
            opacity: 0;
            transform: translateY(20px);
          }
          to {
            opacity: 1;
            transform: translateY(0);
          }
        }
        .animate-slideUp {
          animation: slideUp 0.3s ease-out;
        }
      `}</style>
    </>
  );
}
