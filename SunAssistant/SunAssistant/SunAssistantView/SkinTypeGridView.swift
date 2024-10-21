import SwiftUI
import shared

struct SkinTypeGridView: View {
    @Binding var isPresented: Bool
    @State private var selectedType: SkinType?
    
    var onConfirmSelection: ((SkinType) -> Void)? = nil
    
    init(isPresented: Binding<Bool>) {
        self._isPresented = isPresented
    }
    
    init(isPresented: Binding<Bool>, onConfirmAction: @escaping (SkinType) -> Void) {
        self._isPresented = isPresented
        self.onConfirmSelection = onConfirmAction
    }
    
    private var skinSelectionTitle: some View {
        Text(CommonStrings_User.shared.skin_selection_title)
            .font(.headline)
            .padding(.top, CommonDimen.shared.default_padding)
            .foregroundColor(Color.black)
    }
    
    private var skinSelectionSubtitle: some View {
        Text(CommonStrings_User.shared.skin_selection_subtitle)
            .font(.body)
            .padding(.horizontal, CommonDimen.shared.default_padding)
            .frame(maxWidth: .infinity)
            .foregroundColor(.black)
            .multilineTextAlignment(.center)
            .padding(.top, CommonDimen.shared.default_padding)
    }
    
    private var confirmButton: some View {
        Button(action: confirmSelection) {
            Text(CommonStrings_User.shared.button_confirm)
                .font(.headline)
                .foregroundColor(selectedType != nil ? .white : .gray)
                .frame(maxWidth: .infinity)
                .padding()
        }
        .disabled(selectedType == nil)
        .background(selectedType != nil ? Color.black : Color.gray)
        .cornerRadius(CommonDimen.shared.default_corner_radius)
        .padding(.horizontal, CommonDimen.shared.default_padding)
    }
    
    var body: some View {
        VStack {
            skinSelectionTitle
            skinSelectionSubtitle
            skinTypeGrid
            Spacer()
            confirmButton
                .environment(\.colorScheme, .light)
        }.defaultBackground()
    }
    
    private var skinTypeGrid: some View {
        ScrollViewReader { proxy in
            ScrollView {
                LazyVGrid(columns: [GridItem(.flexible()), GridItem(.flexible())], spacing: CommonDimen.shared.default_padding) {
                    ForEach(SkinType.entries, id: \.self) { type in
                        SkinTypeCell(type: type, isSelected: selectedType == type)
                            .id(type)
                            .onTapGesture { self.selectedType = type }
                            .padding(.horizontal)
                            .fixedSize(horizontal: false, vertical: true)
                    }
                }
            }
            .padding(.top)
            .onAppear { fetchSelectedSkinType(proxy: proxy) }
        }
    }
    
    private func confirmSelection() {
        guard let selectedType = selectedType else { return }
        let storage = IosStorage()
        storage.save(key: CommonStrings_Keys.shared.SKIN_TYPE_KEY, value: selectedType.stringCodeMatch)
        if (self.onConfirmSelection != nil) {
            onConfirmSelection!(selectedType)
        }
        isPresented = false
    }
    
    private func fetchSelectedSkinType(proxy: ScrollViewProxy) {
        selectedType = StorageUtil.getSelectedSkinType()
        if let selected = selectedType {
            withAnimation { proxy.scrollTo(selected, anchor: .center) }
        }
    }
}

struct SkinTypeCell: View {
    let type: SkinType
    var isSelected: Bool
    
    var body: some View {
        VStack {
            colorIndicator
            typeTitle
            typeDescription
        }
        .padding(CommonDimen.shared.padding_8)
        .background(isSelected ? Color.gray : Color.clear)
        .cornerRadius(CommonDimen.shared.default_corner_radius)
    }
    
    private var colorIndicator: some View {
        Rectangle()
            .fill(Color(hex: type.colorReferenceHex))
            .frame(height: CommonDimen.shared.default_height_100)
    }
    
    private var typeTitle: some View {
        Text(type.typeName)
            .fontWeight(.bold)
            .foregroundColor(.black)
            .frame(maxWidth: .infinity, alignment: .leading)
            .frame(height: 15)
    }
    
    private var typeDescription: some View {
        Text(type.typeDescription)
            .frame(maxWidth: .infinity, alignment: .leading)
            .foregroundColor(.black)
            .frame(height: 80)
            .font(.system(size: 14))
    }
}
